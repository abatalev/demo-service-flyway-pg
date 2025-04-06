package com.abatalev.demo.dbservice.utils;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresAdapter {

    private static Logger log = LoggerFactory.getLogger(PostgresAdapter.class);
    private DriverManagerDataSource dataSource;

    public PostgresAdapter() {
        PostgreSQLContainer container = new PostgreSQLContainer<>(
                        DockerImageName.parse("abatalev/things_postgres:0.0.1").asCompatibleSubstituteFor("postgres"))
                .withPassword("postgres");
        container.start();

        String url = "jdbc:postgresql://" + container.getHost() + ":" + container.getMappedPort(5432)
                + "/test_db?loggerLevel=OFF";
        log.info(url);

        System.setProperty("DB_HOST", container.getHost());
        System.setProperty("DB_PORT", "" + container.getMappedPort(5432));
        System.setProperty("DB_USER", "test_user");
        System.setProperty("DB_PASS", "qwerty");

        var flyway = Flyway.configure()
                .locations("filesystem:./target/db/migrations")
                .schemas("test_schema")
                .dataSource(url, "test_admin", "qwerty")
                .load();
        flyway.info();
        flyway.migrate();

        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername("test_admin");
        dataSource.setPassword("qwerty");
        dataSource.setSchema("test_schema");
    }

    public DriverManagerDataSource getDataSource() {
        return dataSource;
    }
}
