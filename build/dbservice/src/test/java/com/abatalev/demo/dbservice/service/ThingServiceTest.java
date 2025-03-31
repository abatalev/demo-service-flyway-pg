package com.abatalev.demo.dbservice.service;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.abatalev.demo.dbservice.model.Thing;
import com.abatalev.demo.dbservice.utils.PostgresAdapter;

@SpringBootTest
public class ThingServiceTest {

    static PostgresAdapter adapter;

    @BeforeAll
    static void init() {
        System.setProperty("OTLP_HOST", "example.com");
        adapter = new PostgresAdapter();
    }

    @Test 
    void checkSaveThing() {
        new ThingService(new JdbcTemplate(adapter.getDataSource())).save(new Thing("1"));
    }

    @Test
    void checkFindAll() {
        new ThingService(new JdbcTemplate(adapter.getDataSource())).findAll();
    }
}
