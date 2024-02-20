package org.example.dao.mapper;

import org.example.model.Book;
import org.example.model.Genre;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenreMapperImplTest {

    @Test
    void testMapRow() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("genre_id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("Test Genre");

        GenreMapperImpl mapper = new GenreMapperImpl();

        Genre genre = mapper.mapRow(resultSet);
        assertEquals(1L, genre.getId());
        assertEquals("Test Genre", genre.getName());
    }

    @Test
    void testMapList() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getLong("genre_id")).thenReturn(1L, 1L, 2L, 2L);
        when(resultSet.getString("name")).thenReturn("Genre 1", "Genre 2");
        when(resultSet.getLong("book_id")).thenReturn(101L, 101L, 102L, 102L);
        when(resultSet.getString("title")).thenReturn("Book 1", "Book 2");
        when(resultSet.getInt("published_year")).thenReturn(2020, 2021);
        when(resultSet.getLong("author_id")).thenReturn(1L, 2L);
        when(resultSet.getString("author_name")).thenReturn("Author 1", "Author 2");

        GenreMapperImpl genreMapper = new GenreMapperImpl();
        List<Genre> genres = genreMapper.mapList(resultSet);

        assertEquals(2, genres.size());

        Genre firstGenre = genres.get(0);
        assertEquals(1L, firstGenre.getId());
        assertEquals("Genre 1", firstGenre.getName());

        Set<Book> firstGenreBooks = firstGenre.getBooks();
        assertEquals(1, firstGenreBooks.size());
        Book firstBook = firstGenreBooks.iterator().next();
        assertEquals(101L, firstBook.getId());
        assertEquals("Book 1", firstBook.getTitle());
        assertEquals(2020, firstBook.getPublishedYear());
        assertEquals(1L, firstBook.getAuthor().getId());
        assertEquals("Author 1", firstBook.getAuthor().getName());

        Genre secondGenre = genres.get(1);
        assertEquals(2L, secondGenre.getId());
        assertEquals("Genre 2", secondGenre.getName());

        Set<Book> secondGenreBooks = secondGenre.getBooks();
        assertEquals(1, secondGenreBooks.size());
        Book secondBook = secondGenreBooks.iterator().next();
        assertEquals(102L, secondBook.getId());
        assertEquals("Book 2", secondBook.getTitle());
        assertEquals(2021, secondBook.getPublishedYear());
        assertEquals(2L, secondBook.getAuthor().getId());
        assertEquals("Author 2", secondBook.getAuthor().getName());
    }
}
