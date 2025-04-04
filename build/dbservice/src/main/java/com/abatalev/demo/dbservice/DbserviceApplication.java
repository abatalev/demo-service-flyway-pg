package com.abatalev.demo.dbservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.spring.web.v3_1.SpringWebTelemetry;

@SpringBootApplication
public class DbserviceApplication {

	@Bean
    public RestTemplate restTemplate(OpenTelemetry openTelemetry) {
        RestTemplate restTemplate = new RestTemplate();
        SpringWebTelemetry telemetry = SpringWebTelemetry.create(openTelemetry);
        restTemplate.getInterceptors().add(telemetry.newInterceptor());
        return restTemplate;
    }

	public static void main(String[] args) {
		SpringApplication.run(DbserviceApplication.class);
	}
}
