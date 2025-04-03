# Demo For Springboot Service with DB

![](diagram.png)

Grafana Dashboard

![](screenshot.png)

## To do

- observability
  - [x] use prometheus for metrics
  - [x] use loki for logs
  - [x] use tempo for traces
  - [x] use otlp-collector as bridge
  - [x] use grafana for ui
  - [ ] use alertmanager for alerts :-)

- [ ] k6 add test for post http://dbservice:8080/things

- [ ] versioning
  - use /api/vX/ for all endpoints

- [ ] add pitest

- [ ] add info of build

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
