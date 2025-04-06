package com.abatalev.demo.dbservice.service;

import static io.qameta.allure.Allure.step;
import static org.junit.Assert.assertEquals;

import com.abatalev.demo.dbservice.model.Owner;
import com.abatalev.demo.dbservice.utils.PostgresAdapter;
import io.qameta.allure.Epic;
import io.qameta.allure.Epics;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class OwnersServiceTest {

    static PostgresAdapter postgres;

    @BeforeAll
    static void init() {
        System.setProperty("OTLP_HOST", "example.com");
        System.setProperty("OTLP_DISABLED", "true");
        postgres = new PostgresAdapter();
    }

    @Epic("Database")
    @Story("New Owner")
    @Test
    void checkSaveIvanov() {
        getService().save(new Owner("petrov", "Petrov"));
    }

    @Epics({@Epic("Database"), @Epic("Stub")})
    @Story("New and Get Owner")
    @Test
    void checkGetOwnerByNickName() {
        OwnersService service = getService();
        step("new Owner", () -> {
            service.save(new Owner("ivanov", "Ivanov"));
        });
        step("Get By NickName Know Owner", () -> {
            Owner owner = service.getByNickName("ivanov");
            assertEquals(0, owner.errCode);
        });
        step("Get By NickName Unknown Owner", () -> {
            Owner owner = service.getByNickName("sidorov");
            assertEquals(2, owner.errCode);
            assertEquals("Owner not found", owner.errMessage);
        });
    }

    private OwnersService getService() {
        return new OwnersService(new JdbcTemplate(postgres.getDataSource()));
    }
}
