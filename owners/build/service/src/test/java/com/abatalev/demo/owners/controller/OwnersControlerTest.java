package com.abatalev.demo.owners.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.abatalev.demo.owners.model.Owner;
import com.abatalev.demo.owners.utils.PostgresAdapter;
import io.qameta.allure.Epic;
import io.qameta.allure.Epics;
import io.qameta.allure.Feature;
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
public class OwnersControlerTest {

    private static Logger log = LoggerFactory.getLogger(OwnersControlerTest.class);

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

    @Epics({@Epic("Database"), @Epic("Web")})
    @Feature("New Owner")
    @Test
    void checkNewOwner() {
        assertThat(restTemplate.postForObject(
                        "http://localhost:" + port + "/owners/", new Owner("ivanov", "Ivanov"), String.class))
                .contains("{\"nickName\":\"ivanov\",\"name\":\"Ivanov\",\"errCode\":0}");
    }

    @Epics({@Epic("Database"), @Epic("Web")})
    @Feature("Get Owner by nickname")
    @Test
    void checkGetOwnerByNickName() throws Exception {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/owners/ivanov", String.class))
                .contains("{\"nickName\":\"ivanov\",\"name\":\"Ivanov\",\"errCode\":0}");
    }
}
