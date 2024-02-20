package org.example.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesUtilTest {

    @Test
    void testGetProperty() {
        assertEquals("jdbc:postgresql://localhost:5432/LibraryDB", PropertiesUtil.get("jdbcUrl"));
        assertEquals("postgres", PropertiesUtil.get("username"));
        assertEquals("admin", PropertiesUtil.get("password"));
        assertEquals("org.postgresql.Driver", PropertiesUtil.get("driverClassName"));
    }

    @Test
    void testMissingProperty() {
        assertNull(PropertiesUtil.get("nonexistentKey"));
    }
}
