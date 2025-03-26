package com.abatalev.demo.dbservice;

import static org.junit.Assert.assertEquals;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class DbTest {

    private static final String DOCKER_IMAGE = "postgres:13";
    private static final String DB_NAME = "postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String INIT_SCRIPT_PATH = "sql/initialize.sql";

    @Container
    private static PostgreSQLContainer container = new PostgreSQLContainer<>(DOCKER_IMAGE)
            .withDatabaseName(DB_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD)
            .withInitScript(INIT_SCRIPT_PATH);

    static DriverManagerDataSource dataSource;       
    
    @BeforeAll
    static void migrate() {
        container.withDatabaseName("testdb");
        var flyway = Flyway.configure()
            .locations("filesystem:./target/db/migrations")
            .schemas("public")
            .dataSource(container.getJdbcUrl(), container.getUsername(), container.getPassword())
            .load();
        flyway.info();
        flyway.migrate();

        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(container.getJdbcUrl());
        dataSource.setUsername(container.getUsername());
        dataSource.setPassword(container.getPassword());
    }

    @Test 
    void checkQuery(){
        int result = new JdbcTemplate(dataSource).queryForObject("SELECT COUNT(*) FROM a", Integer.class);
        assertEquals(0L,result);
    }
}
