package com.abatalev.demo.dbservice.service;

import static org.junit.Assert.assertEquals;

import com.abatalev.demo.dbservice.model.Thing;
import com.abatalev.demo.dbservice.utils.PostgresAdapter;
import com.abatalev.demo.dbservice.utils.StubAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class ThingServiceTest {

    static PostgresAdapter postgres;
    static StubAdapter stub;

    @BeforeAll
    static void init() {
        System.setProperty("OTLP_HOST", "example.com");
        System.setProperty("OTLP_DISABLED", "true");
        postgres = new PostgresAdapter();
        stub = new StubAdapter();
    }

    @Test
    void checkSaveIvanovThing() {
        getService().save("ivanov", new Thing("1"));
    }

    @Test
    void checkSavePetrovThing() {
        assertEquals(
                "Owner not found",
                Assertions.assertThrows(RuntimeException.class, () -> {
                            getService().save("petrov", new Thing("1"));
                        })
                        .getMessage());
    }

    @Test
    void checkFindAll() {
        new ThingService(new JdbcTemplate(postgres.getDataSource()), null).findAll();
    }

    private ThingService getService() {
        return new ThingService(
                new JdbcTemplate(postgres.getDataSource()),
                new OwnerGetter(stub.getHost(), stub.getPort(), new RestTemplate()));
    }
}
