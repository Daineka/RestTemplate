package org.example.dao.mapper;

import org.example.model.Author;
import org.example.model.Book;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookMapperImplTest {

    @Test
    void testMapRow() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("book_id")).thenReturn(1L);
        when(resultSet.getString("title")).thenReturn("Test Book");
        when(resultSet.getInt("published_year")).thenReturn(2022);

        Author author = new Author();
        author.setId(1L);
        author.setName("Test Author");

        when(resultSet.getLong("author_id")).thenReturn(1L);

        BookMapperImpl mapper = new BookMapperImpl();

        Book book = mapper.mapRow(resultSet);
        assertEquals(1L, book.getId());
        assertEquals("Test Book", book.getTitle());
        assertEquals(2022, book.getPublishedYear());
        assertEquals(author.getId(), book.getAuthor().getId());
    }

    @Test
    void testMapList() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getLong("book_id")).thenReturn(1L, 1L, 2L, 2L);
        when(resultSet.getString("title")).thenReturn("Book 1", "Book 2");
        when(resultSet.getInt("published_year")).thenReturn(2020, 2021);
        when(resultSet.getLong("author_id")).thenReturn(1L, 2L);
        when(resultSet.getString("author_name")).thenReturn("Author 1", "Author 2");

        BookMapperImpl bookMapper = new BookMapperImpl();
        List<Book> books = bookMapper.mapList(resultSet);

        assertEquals(2, books.size());

        Book firstBook = books.get(0);
        assertEquals(1L, firstBook.getId());
        assertEquals("Book 1", firstBook.getTitle());
        assertEquals(2020, firstBook.getPublishedYear());
        assertEquals(1L, firstBook.getAuthor().getId());
        assertEquals("Author 1", firstBook.getAuthor().getName());

        Book secondBook = books.get(1);
        assertEquals(2L, secondBook.getId());
        assertEquals("Book 2", secondBook.getTitle());
        assertEquals(2021, secondBook.getPublishedYear());
        assertEquals(2L, secondBook.getAuthor().getId());
        assertEquals("Author 2", secondBook.getAuthor().getName());
    }
}
