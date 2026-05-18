#!/bin/bash

# Run builds in parallel
(
  cd ../things && ./mk.sh
) &

(
  cd ../owners && ./mk.sh
) &

# Wait for both background jobs to finish
wait

docker run --rm -v "$(pwd):/workdir" -w /workdir \
  pipelinecomponents/yamllint:0.35.9 yamllint -c ../.yamllint .

# Run Docker Compose
docker compose up -d --remove-orphans

