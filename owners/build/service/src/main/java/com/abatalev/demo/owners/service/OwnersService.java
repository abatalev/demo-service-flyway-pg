package com.abatalev.demo.owners.service;

import com.abatalev.demo.owners.model.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class OwnersService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OwnersService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Owner getByNickName(String nickName) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT owner_nickname, owner_name" + " FROM owners_schema.owners_info"
                            + " WHERE owner_nickname = ?",
                    (rs, rowNum) -> new Owner(rs.getString("OWNER_NICKNAME"), rs.getString("OWNER_NAME")),
                    nickName);
        } catch (DataAccessException dae) {
            return new Owner(2, "Owner not found");
        }
    }

    public Owner save(Owner owner) {
        try {
            jdbcTemplate.queryForObject(
                    "SELECT owner_nickname, owner_name" + " FROM owners_schema.owners_info"
                            + " WHERE owner_nickname = ?",
                    (rs, rowNum) -> new Owner(rs.getString("OWNER_NICKNAME"), rs.getString("OWNER_NAME")),
                    owner.getNickName());
            return new Owner(1, "Owner already exists");
        } catch (DataAccessException dae) {
            jdbcTemplate.update(
                    "INSERT INTO owners_schema.owners_info (owner_nickname,owner_name) VALUES (?,?)",
                    owner.getNickName(),
                    owner.getName());
            return owner;
        }
    }
}
