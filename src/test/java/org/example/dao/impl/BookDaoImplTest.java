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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookDaoImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private BookDao bookDao;
    private AuthorDao authorDao;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws SQLException {
        postgreSQLContainer.start();

        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();

        dataSource = TestHelper.createDataSource(jdbcUrl, username, password);
        TestHelper.createTable(dataSource);

        bookDao = new BookDaoImpl(dataSource);
        authorDao = new AuthorDaoImpl(dataSource);

    }

    @AfterEach
    void tearDown() {
        postgreSQLContainer.stop();
    }

    @Test
    void testCreateBook() {
        Author author = new Author();
        author.setName("Test Name");
        authorDao.create(author);

        Book book = new Book();
        book.setTitle("Test Title");
        book.setPublishedYear(2000);
        book.setAuthor(author);

        Book createdBook = bookDao.create(book);

        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getTitle());
        assertEquals("Test Title", createdBook.getTitle());
        assertEquals(2000, createdBook.getPublishedYear());
        assertNotNull(createdBook.getAuthor());
    }

    @Test
    void testDeleteBook() {
        Author author = new Author();
        author.setName("Test Name");
        authorDao.create(author);

        Book book = new Book();
        book.setTitle("Test Title");
        book.setPublishedYear(2000);
        book.setAuthor(author);

        Book createdBook = bookDao.create(book);

        Book deletedBook = bookDao.delete(createdBook);

        assertNotNull(deletedBook, "Deleted book should not be null");
        assertEquals(createdBook.getId(), deletedBook.getId(), "Book IDs should match");
        assertEquals(createdBook.getTitle(), deletedBook.getTitle(), "Book titles should match");
        assertEquals(createdBook.getPublishedYear(), deletedBook.getPublishedYear(), "Published years should match");
        assertEquals(createdBook.getAuthor().getId(), deletedBook.getAuthor().getId(), "Author IDs should match");

        Book shouldBeNullBook = bookDao.read(deletedBook.getId(), dataSource);
        assertNull(shouldBeNullBook, "Deleted book should not exist in the database");
    }

    @Test
    void testUpdateBook() {
        Author author = new Author();
        author.setName("Test Name");
        authorDao.create(author);

        Book book = new Book();
        book.setTitle("Test Title");
        book.setPublishedYear(2000);
        book.setAuthor(author);

        Book createdBook = bookDao.create(book);

        createdBook.setTitle("Updated Title");
        createdBook.setPublishedYear(2001);

        Book updatedBook = bookDao.update(createdBook);

        assertNotNull(updatedBook, "Updated book should not be null");
        assertEquals(createdBook.getId(), updatedBook.getId(), "Book IDs should match");
        assertEquals("Updated Title", updatedBook.getTitle(), "Book title should be updated");
        assertEquals(2001, updatedBook.getPublishedYear(), "Book published year should be updated");
        assertEquals(author.getId(), updatedBook.getAuthor().getId(), "Author IDs should match");
    }

    static class TestHelper {
        static DataSource createDataSource(String jdbcUrl, String username, String password) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            return new HikariDataSource(config);
        }

        static void createTable(DataSource dataSource) throws SQLException {
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE authors (author_id SERIAL PRIMARY KEY, name VARCHAR(255) NOT NULL)");
                statement.executeUpdate("CREATE TABLE books (book_id SERIAL PRIMARY KEY, title VARCHAR(255) NOT NULL, published_year INTEGER, author_id INTEGER REFERENCES authors(author_id) ON DELETE CASCADE)");
            }
        }
    }
}