package com.abatalev.demo.stub.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OwnerControllerTest {
    
    @LocalServerPort
	private int port;

    @Autowired
	private TestRestTemplate restTemplate;

    @BeforeAll
    static void init() {
        System.setProperty("OTLP_HOST", "otlp.example.com");
        System.setProperty("OTLP_DISABLED", "true");
    }

    @Test
	void checkGetOwnerIvanov() throws Exception {        
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/owners/ivanov",String.class))
            .contains("{\"nickName\":\"ivanov\",\"name\":\"Ivanov\",\"errCode\":0}");
	}

    @Test
	void checkGetOwnerPetrov() throws Exception {        
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/owners/petrov",String.class))
            .contains("{\"errCode\":2,\"errMessage\":\"Owner not found\"}");
	}
}
