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
    private static PostgreSQLContainer container = new PostgreSQLContainer<>(
                    DockerImageName.parse("abatalev/things_postgres:0.0.1").asCompatibleSubstituteFor("postgres"))
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres");

    @Test
    void migrate() {
        String url = "jdbc:postgresql://" + container.getHost() + ":" + container.getMappedPort(5432)
                + "/things_db?loggerLevel=OFF";
        var flyway = Flyway.configure()
                .locations("filesystem:src/sql")
                .schemas("things_schema")
                .dataSource(url, "things_admin", "qwerty")
                .load();
        flyway.info();
        flyway.migrate();
    }
}
