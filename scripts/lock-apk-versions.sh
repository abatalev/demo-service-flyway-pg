#!/bin/bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
REPO_DIR="$(dirname "$SCRIPT_DIR")"

DRY_RUN=false

for arg in "$@"; do
    case "$arg" in
        --dry-run) DRY_RUN=true ;;
        --help|-h)
            echo "Usage: $0 [--dry-run]"
            echo ""
            echo "Lock apk package versions in all Dockerfiles."
            echo "Pins unpinned packages and updates existing pins."
            echo ""
            echo "Options:"
            echo "  --dry-run  Show changes without modifying files"
            exit 0
            ;;
        *)
            echo "Unknown option: $arg" >&2
            echo "Usage: $0 [--dry-run]" >&2
            exit 1
            ;;
    esac
done

TMPDIR=$(mktemp -d)
trap 'rm -rf "$TMPDIR"' EXIT

find "$REPO_DIR" -name 'Dockerfile*' -not -path '*/\.git/*' | sort | while IFS= read -r DF; do
    REL="${DF#"$REPO_DIR"/}"
    SAFE="${REL//\//__}"
    echo "=== $REL ==="

    # Join continuation lines into logical lines
    awk '
        /\\$/ { buf = buf substr($0, 1, length($0) - 1); next }
        { print buf $0; buf = "" }
        END { if (buf != "") print buf }
    ' "$DF" > "$TMPDIR/joined"

    # Split into stages by ^FROM, saving FROM line per stage
    awk -v tmpdir="$TMPDIR" -v safe="$SAFE" '
        BEGIN { stage = -1 }
        /^FROM / {
            if (stage >= 0) close(out)
            close(from_out)
            stage++
            out = tmpdir "/stage_" safe "_" stage ".txt"
            from_out = tmpdir "/stage_from_" safe "_" stage ".txt"
            print $0 > from_out
            close(from_out)
            next
        }
        { if (stage >= 0) print >> out }
        END { if (stage >= 0) close(out) }
    ' "$TMPDIR/joined"

    ANY_CHANGES=false

    for STAGE_FILE in "$TMPDIR"/stage_"$SAFE"_*.txt; do
        [ -f "$STAGE_FILE" ] || continue
        STAGE_NUM="${STAGE_FILE##*_}"
        STAGE_NUM="${STAGE_NUM%.txt}"

        # Extract Alpine version from this stage's own FROM line
        FROM_FILE="$TMPDIR/stage_from_${SAFE}_${STAGE_NUM}.txt"
        ALPINE_VER=""
        if [ -f "$FROM_FILE" ]; then
            FROM_LINE=$(cat "$FROM_FILE")
            ALPINE_VER=$(echo "$FROM_LINE" | grep -oP '^FROM\s+([^/]+/)?alpine:\K[0-9]+\.[0-9]+(\.[0-9]+)?' || true)
            if [ -z "$ALPINE_VER" ]; then
                if echo "$FROM_LINE" | grep -qP '^FROM\s+([^/]+/)?alpine([:\s]|$)'; then
                    ALPINE_VER="latest"
                fi
            fi
        fi

        APK_LINES=$(grep 'apk.*add' "$STAGE_FILE" || true)
        if [ -z "$APK_LINES" ]; then
            echo "  -> stage $STAGE_NUM: no apk add"
            continue
        fi

        if [ -z "$ALPINE_VER" ]; then
            echo "  -> stage $STAGE_NUM: no alpine base, unpinning versions"
            # Remove version pins from apk add lines only in this stage
            awk -v target_stage="$STAGE_NUM" '
                /^FROM / { from_count++ }
                /apk.*add/ && from_count == target_stage + 1 {
                    gsub(/=[0-9a-zA-Z._~][0-9a-zA-Z._~-]*/, "")
                    changed = 1
                }
                { print }
            ' "$TMPDIR/joined" > "$TMPDIR/joined.new" && mv "$TMPDIR/joined.new" "$TMPDIR/joined"
            cp "$TMPDIR/joined" "$DF"
            ANY_CHANGES=true
            if [ "${changed:-0}" = "1" ]; then
                echo "    -> versions unpinned"
            else
                echo "    -> no pinned versions"
            fi
            continue
        fi

        echo "  -> stage $STAGE_NUM: apk add detected (alpine $ALPINE_VER)"

        # Extract unique package names (strip existing =version)
        PKGS=$(
            echo "$APK_LINES" \
                | sed 's/\\//g; s/.*\bapk\b.*\badd\b\s*//; s/\s*&&.*//; s/--no-cache //g; s/--cache-dir[^[:space:]]* //g; s/--update //g' \
                | tr ' \t' '\n' \
                | grep -v '^[[:space:]]*$' \
                | sed 's/=.*//' \
                | sort -u
        )
        [ -n "$PKGS" ] || { echo "    -> no packages extracted"; continue; }

        # Build temp Dockerfile to resolve latest versions
        TMP_DF="$TMPDIR/Dockerfile.$SAFE.stage$STAGE_NUM"
        {
            echo "FROM alpine:$ALPINE_VER"
            echo "RUN apk add --no-cache \\"
            while IFS= read -r PKG; do
                PKG="${PKG%"${PKG##*[![:space:]]}"}"
                [ -n "$PKG" ] || continue
                echo "    $PKG \\"
            done <<< "$PKGS"
            echo "    && true"
        } > "$TMP_DF"

        echo "    -> building image..."
        IMAGE_TAG="lock-$(echo "$SAFE-$STAGE_NUM" | md5sum | head -c 8)"
        if ! docker build -f "$TMP_DF" -t "$IMAGE_TAG" "$REPO_DIR" 2>&1 | tail -5; then
            echo "    -> build failed"
            continue
        fi

        echo "    -> extracting installed versions..."
        docker run --rm "$IMAGE_TAG" cat /lib/apk/db/installed \
            | awk -v RS= '{ for(i=1;i<=NF;i++){ if($i ~ /^P:/) pkg=substr($i,3); if($i ~ /^V:/) ver=substr($i,3) } if(pkg && ver) print pkg, ver; pkg=""; ver="" }' \
            | sort > "$TMPDIR/versions.$SAFE.$STAGE_NUM"

        # Log changes
        HAS_CHANGES=false
        while IFS=' ' read -r PKG VER; do
            [ -n "$PKG" ] || continue
            OLD_VER=$(grep -oP "(?<=\b$PKG=)[0-9a-zA-Z._~-]+" "$DF" | head -1 || true)
            if [ -n "$OLD_VER" ]; then
                [ "$OLD_VER" = "$VER" ] && { echo "    $PKG: $OLD_VER (current)"; continue; }
                echo "    $PKG: $OLD_VER -> $VER"
            else
                echo "    $PKG: unpinned -> $VER"
            fi
            HAS_CHANGES=true
        done < "$TMPDIR/versions.$SAFE.$STAGE_NUM"

        if ! $HAS_CHANGES; then
            echo "    -> up to date"
            docker rmi "$IMAGE_TAG" > /dev/null 2>&1 || true
            continue
        fi

        if $DRY_RUN; then
            echo "    -> dry run, skipped"
            docker rmi "$IMAGE_TAG" > /dev/null 2>&1 || true
            continue
        fi

        # Apply replacements in one awk pass
        awk -v mapfile="$TMPDIR/versions.$SAFE.$STAGE_NUM" '
            BEGIN {
                while ((getline line < mapfile) > 0) {
                    split(line, parts)
                    map[parts[1]] = parts[2]
                }
                close(mapfile)
            }
            /apk.*add/ {
                out = ""
                rest = $0
                while (match(rest, /[^ \t]+/)) {
                    token = substr(rest, RSTART, RLENGTH)
                    pre = substr(rest, 1, RSTART - 1)
                    split(token, parts, "=")
                    if (parts[1] in map) {
                        token = parts[1] "=" map[parts[1]]
                    }
                    out = out pre token
                    rest = substr(rest, RSTART + RLENGTH)
                }
                $0 = out
            }
            { print }
        ' "$TMPDIR/joined" > "$TMPDIR/joined.new" && mv "$TMPDIR/joined.new" "$TMPDIR/joined"

        cp "$TMPDIR/joined" "$DF"
        ANY_CHANGES=true

        docker rmi "$IMAGE_TAG" > /dev/null 2>&1 || true
        echo "    -> done"
    done

    ! $ANY_CHANGES && echo "  -> no changes"
done

echo ""
$DRY_RUN && echo "Dry run complete. No files modified." || echo "All versions locked!"
