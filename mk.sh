#!/bin/sh
VERSION="0.0.1"
CDIR=$(pwd)

build_docker_image() {
    LABEL=$1
    NAME=$2

    echo "### create docker image for ${LABEL}"
    cd "${CDIR}/build" || exit
    docker run --rm -i hadolint/hadolint:2.12.1-beta-alpine < "Dockerfile.${NAME}"
    if ! docker buildx build -f "Dockerfile.${NAME}" -t "abatalev/${NAME}:${VERSION}" .; then
        echo "### aborted - ${LABEL}"
        exit
    fi
}

build_maven() {
    LABEL=$1
    NAME=$2

    echo "### build jar for ${LABEL}"
    cd "${CDIR}/build/${NAME}" || exit
    if ! mvn clean install; then
        echo "### aborted - ${LABEL}"
        exit
    fi
}

clean_maven() {
    NAME=$1

    cd "${CDIR}/build/${NAME}" || exit
    mvn clean
}

echo "Demo Service with Stub, Postgres and Flyway"

find . -name '*.sh' \
  -exec docker run --rm -it -v "${CDIR}:/mnt" koalaman/shellcheck:v0.10.0 {} \;

build_docker_image "db" "postgres"

build_maven "initdb" "initdb"
build_docker_image "initdb" "initdb"

build_maven "stub" "stub"
build_docker_image "stub" "stub"

build_maven "dbservice" "dbservice"
build_docker_image "dbservice" "dbservice"

# cd "${CDIR}/build/initdb" || exit
# mvn clean test jacoco:report org.pitest:pitest-maven:mutationCoverage org.pitest:pitest-maven:report allure:report

# cd "${CDIR}/build/stub" || exit
# mvn clean test jacoco:report org.pitest:pitest-maven:mutationCoverage org.pitest:pitest-maven:report allure:report

# cd "${CDIR}/build/dbservice" || exit
# mvn clean test jacoco:report org.pitest:pitest-maven:mutationCoverage org.pitest:pitest-maven:report allure:report

build_docker_image "builddocs" "builddocs"

clean_maven "initdb"
clean_maven "stub"
clean_maven "dbservice"

#docker run --rm -p 8087:80 abatalev/builddocs:0.0.1 
#docker run --rm -p 8080:8080 abatalev/dbservice:0.0.1