package com.abatalev.demo.initdb;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class InitDbTest {

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
    
    @Test 
    void migrate() {
        container.withDatabaseName("testdb");
        var flyway = Flyway.configure()
            .locations("filesystem:src/sql")
            .schemas("public")
            .dataSource(container.getJdbcUrl(), container.getUsername(), container.getPassword())
            .load();
        flyway.info();
        flyway.migrate();
    }
}
