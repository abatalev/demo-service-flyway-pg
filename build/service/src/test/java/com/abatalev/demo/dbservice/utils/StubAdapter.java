package com.abatalev.demo.dbservice.utils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

public class StubAdapter {
    private GenericContainer container;

    public StubAdapter() {
        container = new GenericContainer("abatalev/things_stub:0.0.1")
                .withExposedPorts(8080)
                .withEnv("OTLP_HOST", "otlp.example.com")
                .withEnv("OTLP_DISABLED", "true")
                .waitingFor(new LogMessageWaitStrategy()
                        .withRegEx(".*Started StubApplication.*\\s")
                        .withTimes(1)
                        .withStartupTimeout(Duration.of(20, ChronoUnit.SECONDS)));
        container.start();
    }

    public String getHost() {
        return container.getHost();
    }

    public String getPort() {
        return "" + container.getMappedPort(8080);
    }
}
