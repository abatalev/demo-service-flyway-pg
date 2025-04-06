package com.abatalev.demo.dbservice;

import io.qameta.allure.Epic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DbserviceApplicationTests {

    @BeforeAll
    static void init() {
        System.setProperty("OTLP_HOST", "otlp.example.com");
        System.setProperty("OTLP_DISABLED", "true");
        System.setProperty("OWNER_HOST", "owner.example.com");
        System.setProperty("OWNER_PORT", "8080");
    }

    @Epic("Web")
    @Test
    void contextLoads() {}
}
