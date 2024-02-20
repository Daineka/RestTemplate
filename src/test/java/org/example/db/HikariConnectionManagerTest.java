package org.example.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertNull;

class HikariConnectionManagerTest {

    private PostgreSQLContainer<?> postgresContainer;

    @BeforeEach
    void setUp() {
        postgresContainer = new PostgreSQLContainer<>("postgres:13")
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test");
        postgresContainer.start();
        System.setProperty("jdbcUrl", postgresContainer.getJdbcUrl());
        System.setProperty("username", postgresContainer.getUsername());
        System.setProperty("password", postgresContainer.getPassword());
        System.setProperty("driverClassName", postgresContainer.getDriverClassName());
    }

    @Test
    void testCloseDataSource() {
        HikariConnectionManager.closeDataSource();
        assertNull(HikariConnectionManager.getDataSource());
    }

    @AfterEach
    void tearDown() {
        if (postgresContainer != null && postgresContainer.isRunning()) {
            postgresContainer.stop();
        }
    }
}
