package com.abatalev.demo.things.service;

import com.abatalev.demo.things.model.Owner;
import com.abatalev.demo.things.model.Thing;
import io.micrometer.core.annotation.Timed;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ThingService {

    private final JdbcTemplate jdbcTemplate;
    private final OwnerGetter getter;

    @Autowired
    public ThingService(JdbcTemplate jdbcTemplate, OwnerGetter getter) {
        this.jdbcTemplate = jdbcTemplate;
        this.getter = getter;
    }

    public void save(String nickName, Thing thing) {
        Owner owner = getter.get(nickName);
        jdbcTemplate.update(
                "INSERT INTO a (aa,owner_nick,owner_name) VALUES (?,?,?)", thing.getName(), owner.nickName, owner.name);
    }

    @Timed
    public List<Thing> findAll() {
        return (List<Thing>) jdbcTemplate.query(
                "SELECT aa, owner_nick, owner_name FROM things_schema.a",
                (rs, rowNum) -> new Thing(rs.getString("AA")));
    }
}
