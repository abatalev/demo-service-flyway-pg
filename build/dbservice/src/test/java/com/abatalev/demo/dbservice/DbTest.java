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
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class DbTest {

    @Container
    private static PostgreSQLContainer container = new PostgreSQLContainer<>(
        DockerImageName
            .parse("abatalev/postgres:0.0.1")
            .asCompatibleSubstituteFor("postgres"))
            .withPassword("postgres");

    static DriverManagerDataSource dataSource;       
    
    @BeforeAll
    static void migrate() {

        String url =  "jdbc:postgresql://"+container.getHost()+":"+container.getMappedPort(5432)+"/test_db?loggerLevel=OFF";

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

    @Test 
    void checkQuery(){
        int result = new JdbcTemplate(dataSource).queryForObject("SELECT COUNT(*) FROM a", Integer.class);
        assertEquals(0L,result);
    }        
}
