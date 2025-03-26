#!/bin/sh
VERSION="0.0.1"
CDIR=$(pwd)

echo "Demo DbService with Flyway"

echo "### buiild jar for dbservice"
cd "${CDIR}/build/dbservice" || exit
mvn clean install

echo "### create docker image for dbservice"
cd "${CDIR}/build" || exit
if ! docker buildx build -f Dockerfile.dbservice -t "abatalev/dbservice:${VERSION}" .; then
    echo "### aborted"
fi

echo "### buiild jar for initdb"
cd "${CDIR}/build/initdb" || exit
mvn clean install

echo "### create docker image for initdb"
cd "${CDIR}/build" || exit
if ! docker buildx build -f Dockerfile.initdb -t "abatalev/initdb:${VERSION}" .; then
    echo "### aborted"
fi

#docker run --rm -p 8080:8080 abatalev/dbservice:0.0.1