package com.abatalev.demo.dbservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.abatalev.demo.dbservice.utils.PostgresAdapter;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ThingControlerTest {

    static private Logger log = LoggerFactory.getLogger(ThingControlerTest.class);

    @LocalServerPort
	private int port;

    @Autowired
	private TestRestTemplate restTemplate;

    static PostgresAdapter adapter;

    @BeforeAll
    static void init() {
        System.setProperty("OTLP_HOST", "example.com");
		System.setProperty("OTLP_DISABLED", "true");
        log.info("init - started");
        adapter = new PostgresAdapter();
        log.info("init - done");
    }

    @Test
	void checkGetThings() throws Exception {        
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/things",String.class))
            .contains("[]");
	}
}
