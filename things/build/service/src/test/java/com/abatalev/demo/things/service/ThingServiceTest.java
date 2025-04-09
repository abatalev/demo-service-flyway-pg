package com.abatalev.demo.things.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.abatalev.demo.things.model.Thing;
import com.abatalev.demo.things.utils.PostgresAdapter;
import com.abatalev.demo.things.utils.StubAdapter;
import io.qameta.allure.Epic;
import io.qameta.allure.Epics;
import io.qameta.allure.Story;
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

    @Epics({@Epic("Database"), @Epic("Stub")})
    @Story("Known Owner")
    @Test
    void checkSaveIvanovThing() {
        getService().save("ivanov", new Thing("1"));
    }

    @Epics({@Epic("Database"), @Epic("Stub")})
    @Story("Unknown Owner")
    @Test
    void checkSavePetrovThing() {
        assertEquals(
                "Owner not found",
                assertThrows(RuntimeException.class, () -> {
                            getService().save("petrov", new Thing("1"));
                        })
                        .getMessage());
    }

    @Epic("Database")
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
