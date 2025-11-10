package com.abatalev.demo.owners;

import io.opentelemetry.api.OpenTelemetry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class OwnersApplication {

    @Bean
    public RestClient restClient(OpenTelemetry openTelemetry) {
        return RestClient.create();
    }

    public static void main(String[] args) {
        SpringApplication.run(OwnersApplication.class);
    }
}
