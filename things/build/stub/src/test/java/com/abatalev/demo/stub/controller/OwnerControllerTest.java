package com.abatalev.demo.stub.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OwnerControllerTest {

    @LocalServerPort
    private int port;

    private RestClient restClient = RestClient.create();

    @BeforeAll
    static void init() {
        System.setProperty("OTLP_HOST", "otlp.example.com");
        System.setProperty("OTLP_DISABLED", "true");
    }

    @Test
    void checkGetOwnerIvanov() throws Exception {
        assertThat(restClient
                        .get()
                        .uri("http://localhost:" + port + "/owners/ivanov")
                        .retrieve()
                        .toEntity(String.class)
                        .getBody())
                .contains("{\"nickName\":\"ivanov\",\"name\":\"Ivanov\",\"errCode\":0}");
    }

    @Test
    void checkGetOwnerPetrov() throws Exception {
        assertThat(restClient
                        .get()
                        .uri("http://localhost:" + port + "/owners/petrov")
                        .retrieve()
                        .toEntity(String.class)
                        .getBody())
                .contains("{\"errCode\":2,\"errMessage\":\"Owner not found\"}");
    }
}
