# Demo For Springboot Service with DB

## To do

- observability
  - [x] use prometheus for metrics
  - [x] use loki for logs
  - [x] use tempo for traces
  - [x] use otlp-collector as bridge
  - [x] use grafana for ui
  - [ ] use alertmanager for alerts :-)

- [ ] use stub-service
  - add stub
    - [mock-server](https://www.mock-server.com/) ?
  - modify dbservice
    - use webclient (get author name by id) > get http://stub/author/{i}

- [ ] versioning
  - use /api/vX/ for all endpoints

- [ ] documentation
  - architecture 

## Build

```sh
./mk.sh
docker compose up
```

## Pages

 - http://localhost:55679/debug/tracez


## See also

- https://vorozco.com/blog/2024/2024-11-18-A-practical-guide-spring-boot-open-telemetry.html
- https://ridakaddir.com/blog/post/java-observability-using-opentelemetry-tempo-and-loki
- https://www.dmosk.ru/miniinstruktions.php?mini=prometheus-stack-docker
