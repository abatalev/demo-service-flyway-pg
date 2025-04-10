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

# Run Docker Compose
docker compose up -d --remove-orphans

