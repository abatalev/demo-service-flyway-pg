package com.abatalev.demo.things;

import io.opentelemetry.api.OpenTelemetry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class ThingsApplication {

    @Bean
    public RestClient restClient(OpenTelemetry openTelemetry) {
        return RestClient.create();
    }

    public static void main(String[] args) {
        SpringApplication.run(ThingsApplication.class);
    }
}
