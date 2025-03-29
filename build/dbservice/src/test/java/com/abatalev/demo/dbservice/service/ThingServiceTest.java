package com.abatalev.demo.dbservice.service;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.abatalev.demo.dbservice.model.Thing;
import com.abatalev.demo.dbservice.utils.PostgresAdapter;

@SpringBootTest
public class ThingServiceTest {

    static PostgresAdapter adapter;

    @BeforeAll
    static void init() {
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
