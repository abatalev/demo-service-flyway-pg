package com.abatalev.demo.things;

import static org.junit.Assert.assertEquals;

import com.abatalev.demo.things.utils.PostgresAdapter;
import io.qameta.allure.Epic;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class DbTest {

    @ClassRule
    private static PostgresAdapter adapter;

    @BeforeAll
    static void migrate() {
        adapter = new PostgresAdapter();
    }

    @Epic("Database")
    @Test
    void checkQuery() {
        int result = new JdbcTemplate(adapter.getDataSource()).queryForObject("SELECT COUNT(*) FROM a", Integer.class);
        assertEquals(0L, result);
    }
}
