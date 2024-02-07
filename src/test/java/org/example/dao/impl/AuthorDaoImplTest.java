package org.example.dao.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.dao.AuthorDao;
import org.example.dao.BookDao;
import org.example.model.Author;
import org.example.model.Book;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthorDaoImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private AuthorDao authorDao;
    private BookDao bookDao;
    private DataSource dataSource;
    @BeforeEach
    void setUp() throws SQLException {
        postgreSQLContainer.start();

        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();

        dataSource = TestHelper.createDataSource(jdbcUrl, username, password);
        TestHelper.createTable(dataSource);

        authorDao = new AuthorDaoImpl(dataSource);
        bookDao = new BookDaoImpl(dataSource);
    }

    @AfterEach
    void tearDown() {
        postgreSQLContainer.stop();
    }

    @Test
    void testRead() {
        Author author = new Author();
        author.setName("Test Author");

        Author createdAuthor = authorDao.create(author);

        Long authorId = createdAuthor.getId();

        Author retrievedAuthor = authorDao.read(authorId, dataSource);

        assertNotNull(retrievedAuthor);
        assertEquals("Test Author", retrievedAuthor.getName());
    }

    @Test
    void testFindAll() {
        Author author1 = new Author();
        author1.setName("Author 1");
        Author author2 = new Author();
        author2.setName("Author 2");
        authorDao.create(author1);
        authorDao.create(author2);

        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setPublishedYear(2020);
        book1.setAuthor(author1);
        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setPublishedYear(2021);
        book2.setAuthor(author1);
        bookDao.create(book1);
        bookDao.create(book2);

        List<Author> authors = authorDao.findAll(dataSource);

        System.out.println(authors);

        assertNotNull(authors);
        assertEquals(2, authors.size());
    }

    @Test
    void testCreateAuthor() {
        Author author = new Author();
        author.setName("Test Name");

        Author createdAuthor = authorDao.create(author);

        assertNotNull(createdAuthor.getId());
        assertEquals("Test Name", createdAuthor.getName());
    }

    @Test
    void testUpdateAuthor() {
        Author author = new Author();
        author.setName("Test Name");

        Author createdAuthor = authorDao.create(author);
        createdAuthor.setName("Updated Name");

        Author updatedAuthor = authorDao.update(createdAuthor);

        assertNotNull(updatedAuthor, "Updated author should not be null");
        assertEquals("Updated Name", updatedAuthor.getName());
    }

    @Test
    void testDeleteAuthor() {
        Author author = new Author();
        author.setName("Test Name");

        Author createdAuthor = authorDao.create(author);

        Author deletedAuthor = authorDao.delete(createdAuthor);

        assertNotNull(deletedAuthor, "Deleted author should not be null");
        assertEquals(createdAuthor.getId(), deletedAuthor.getId(), "Author IDs should match");
        assertEquals(createdAuthor.getName(), deletedAuthor.getName(), "Author names should match");

        Author shouldBeNullAuthor = authorDao.read(deletedAuthor.getId(), dataSource);
        assertNull(shouldBeNullAuthor, "Deleted author should not exist in the database");
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
                statement.executeUpdate("DROP TABLE IF EXISTS authors, books, genres, bookgenres CASCADE");
                statement.executeUpdate("CREATE TABLE authors (author_id SERIAL PRIMARY KEY, name VARCHAR(255) NOT NULL)");
                statement.executeUpdate("CREATE TABLE books (book_id SERIAL PRIMARY KEY, title VARCHAR(255) NOT NULL, published_year INTEGER, author_id INTEGER REFERENCES authors(author_id) ON DELETE CASCADE)");
            }
        }
    }
}
