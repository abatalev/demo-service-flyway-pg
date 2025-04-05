package com.abatalev.demo.dbservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.abatalev.demo.dbservice.model.Thing;
import com.abatalev.demo.dbservice.utils.PostgresAdapter;
import com.abatalev.demo.dbservice.utils.StubAdapter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ThingControlerTest {

    private static Logger log = LoggerFactory.getLogger(ThingControlerTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    static PostgresAdapter adapter;
    static StubAdapter stub;

    @BeforeAll
    static void init() {
        System.setProperty("OTLP_HOST", "example.com");
        System.setProperty("OTLP_DISABLED", "true");
        log.info("init - started");
        adapter = new PostgresAdapter();
        stub = new StubAdapter();
        System.setProperty("OWNER_HOST", stub.getHost());
        System.setProperty("OWNER_PORT", stub.getPort());
        log.info("init - done");
    }

    @Test
    void checkNewThing() {
        assertThat(restTemplate.postForObject(
                        "http://localhost:" + port + "/things/ivanov", new Thing("GummyBear"), String.class))
                .contains("{\"name\":\"GummyBear\"}");
    }

    @Test
    void checkGetThings() throws Exception {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/things", String.class))
                .contains("[]");
    }
}
