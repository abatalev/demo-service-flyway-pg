#!/bin/sh
VERSION="0.0.1"
GROUPNAME="owners"
CDIR=$(pwd)

build_docker_image() {
    LABEL=$1
    NAME=$2

    echo "### create docker image for ${LABEL}"
    cd "${CDIR}/build" || exit
    docker run --rm -i hadolint/hadolint:2.12.1-beta-alpine < "Dockerfile.${NAME}"
    if ! docker buildx build -f "Dockerfile.${NAME}" -t "abatalev/${GROUPNAME}_${NAME}:${VERSION}" .; then
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

build_component() {
    LABEL=$1
    NAME=$2

    if [ -f "${CDIR}/build/${NAME}/pom.xml" ]; then
        build_maven "${LABEL}" "${NAME}"
    fi
    build_docker_image "${LABEL}" "${NAME}"
}

clean_maven() {
    NAME=$1

    cd "${CDIR}/build/${NAME}" || exit
    mvn clean
}

echo "Demo Service with Stub, Postgres and Flyway"

find . -name '*.sh' \
  -exec docker run --rm -it -v "${CDIR}:/mnt" koalaman/shellcheck:v0.10.0 {} \;

build_component "db" "postgres"
build_component "initdb" "initdb"
build_component "service" "service"

# cd "${CDIR}/build/initdb" || exit
# mvn clean test jacoco:report org.pitest:pitest-maven:mutationCoverage org.pitest:pitest-maven:report allure:report

# cd "${CDIR}/build/stub" || exit
# mvn clean test jacoco:report org.pitest:pitest-maven:mutationCoverage org.pitest:pitest-maven:report allure:report

# cd "${CDIR}/build/dbservice" || exit
# mvn clean test jacoco:report org.pitest:pitest-maven:mutationCoverage org.pitest:pitest-maven:report allure:report

build_component "builddocs" "builddocs"

# clean_maven "initdb"
# clean_maven "service"

#docker run --rm -p 8087:80 abatalev/builddocs:0.0.1 
#docker run --rm -p 8080:8080 abatalev/service:0.0.1