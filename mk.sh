#!/bin/sh
VERSION="0.0.1"
CDIR=$(pwd)

echo "Demo DbService with Flyway"

echo "### create docker image for db"
cd "${CDIR}/build" || exit
if ! docker buildx build -f Dockerfile.postgres -t "abatalev/postgres:${VERSION}" .; then
    echo "### aborted - db"
    exit
fi

echo "### build jar for initdb"
cd "${CDIR}/build/initdb" || exit
if ! mvn clean install; then
    echo "### aborted - initdb"
    exit
fi

echo "### create docker image for initdb"
cd "${CDIR}/build" || exit
if ! docker buildx build -f Dockerfile.initdb -t "abatalev/initdb:${VERSION}" .; then
    echo "### aborted - abatalev/initdb"
    exit
fi

echo "### build jar for stub"
cd "${CDIR}/build/stub" || exit
if ! mvn clean install; then
    echo "### aborted - stub"
    exit
fi

echo "### create docker image for stub"
cd "${CDIR}/build" || exit
if ! docker buildx build -f Dockerfile.stub -t "abatalev/stub:${VERSION}" .; then
    echo "### aborted - abatalev/stub"
    exit
fi

echo "### build jar for dbservice"
cd "${CDIR}/build/dbservice" || exit
if ! mvn clean install; then
    echo "### aborted - dbservice"
    exit
fi

echo "### create docker image for dbservice"
cd "${CDIR}/build" || exit
if ! docker buildx build -f Dockerfile.dbservice -t "abatalev/dbservice:${VERSION}" .; then
    echo "### aborted - abatalev/dbservice"
    exit
fi

#docker run --rm -p 8080:8080 abatalev/dbservice:0.0.1