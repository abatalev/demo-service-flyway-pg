package com.abatalev.demo.dbservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.abatalev.demo.dbservice.model.Thing;

@Service
public class ThingService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ThingService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Thing thing) {
        jdbcTemplate.update("INSERT INTO a (aa) VALUES (?)", thing.name);
    }

    public List<Thing> findAll() {
        return (List<Thing>) jdbcTemplate.query("SELECT aa FROM test_schema.a", (rs, rowNum) -> new Thing(rs.getString("AA")) );
    }
}