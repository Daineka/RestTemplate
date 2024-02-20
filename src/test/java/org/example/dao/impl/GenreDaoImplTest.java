package org.example.dao.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.dao.GenreDao;
import org.example.model.Genre;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenreDaoImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");
    private GenreDao genreDao;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws SQLException {
        postgreSQLContainer.start();

        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();

        dataSource = TestHelper.createDataSource(jdbcUrl, username, password);
        TestHelper.createTable(dataSource);

        genreDao = new GenreDaoImpl(dataSource);
    }

    @AfterEach
    void tearDown() {
        postgreSQLContainer.stop();
    }

    @Test
    void testCreateGenre() {
        Genre genre = new Genre();
        genre.setName("Thriller");

        Genre createdGenre = genreDao.create(genre);

        assertNotNull(createdGenre.getId());
        assertEquals("Thriller", createdGenre.getName());
    }

    @Test
    void testUpdateGenre() {
        Genre genre = new Genre();
        genre.setName("Comedy");

        Genre createdGenre = genreDao.create(genre);
        createdGenre.setName("Romance");

        Genre updatedGenre = genreDao.update(createdGenre);

        assertNotNull(updatedGenre, "Updated genre should not be null");
        assertEquals("Romance", updatedGenre.getName());
    }

    @Test
    void testDeleteGenre() {
        Genre genre = new Genre();
        genre.setName("Horror");

        Genre createdGenre = genreDao.create(genre);

        Genre deletedGenre = genreDao.delete(createdGenre);

        assertNotNull(deletedGenre, "Deleted genre should not be null");
        assertEquals(createdGenre.getId(), deletedGenre.getId(), "Genre IDs should match");
        assertEquals(createdGenre.getName(), deletedGenre.getName(), "Genre names should match");

        Genre shouldBeNullGenre = genreDao.read(deletedGenre.getId(), dataSource);
        assertNull(shouldBeNullGenre, "Deleted genre should not exist in the database");
    }

    public static class TestHelper {

        static DataSource createDataSource(String jdbcUrl, String username, String password) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            return new HikariDataSource(config);
        }

        public static void createTable(DataSource dataSource) throws SQLException {
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE genres (genre_id SERIAL PRIMARY KEY, name VARCHAR(255) NOT NULL)");
            }
        }
    }
}
