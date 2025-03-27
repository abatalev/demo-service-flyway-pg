package com.abatalev.demo.initdb;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class InitDbTest {

    @Container
    private static PostgreSQLContainer container = 
            new PostgreSQLContainer<>(DockerImageName
                .parse("abatalev/postgres:0.0.1")
                .asCompatibleSubstituteFor("postgres"))
            .withPassword("postgres");
    
    @Test 
    void migrate() {

        String url =  "jdbc:postgresql://"+container.getHost()+":"+container.getMappedPort(5432)+"/test_db?loggerLevel=OFF";
        System.err.println("URL-1: "+ container.getJdbcUrl());
        System.err.println("URL-2: "+ url);

        var flyway = Flyway.configure()
            .locations("filesystem:src/sql")
            .schemas("test_schema")
            .dataSource(url, "test_admin", "qwerty")
            .load();
        flyway.info();
        flyway.migrate();
    }
}
