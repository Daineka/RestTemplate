package org.example.dao.mapper;

import org.example.model.Author;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorMapperImplTest {

    @Test
    void testMapRow() throws SQLException {
        ResultSet resultSet = createResultSet(1L, "Test Author");

        AuthorMapperImpl mapper = new AuthorMapperImpl();

        Author author = mapper.mapRow(resultSet);
        assertEquals(1L, author.getId());
        assertEquals("Test Author", author.getName());
    }

    @Test
    void testMapList() throws SQLException {
        ResultSet resultSet = createResultSetForList();

        AuthorMapperImpl authorMapper = new AuthorMapperImpl();
        List<Author> authors = authorMapper.mapList(resultSet);

        assertEquals(2, authors.size());

        assertEquals(2, authors.get(0).getBooks().size());
        assertEquals(2, authors.get(1).getBooks().size());

        assertEquals(1L, authors.get(0).getId());
        assertEquals("Author 1", authors.get(0).getName());
        assertEquals(2L, authors.get(1).getId());
        assertEquals("Author 2", authors.get(1).getName());

        assertEquals(103L, authors.get(0).getBooks().iterator().next().getId());
        assertEquals("Book 3", authors.get(0).getBooks().iterator().next().getTitle());
        assertEquals(104L, authors.get(1).getBooks().iterator().next().getId());
        assertEquals("Book 4", authors.get(1).getBooks().iterator().next().getTitle());
    }

    private ResultSet createResultSet(long authorId, String authorName) throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("author_id")).thenReturn(authorId);
        when(resultSet.getString("name")).thenReturn(authorName);
        return resultSet;
    }

    private ResultSet createResultSetForList() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.next()).thenReturn(true, true, true, true, false);
        when(resultSet.getLong("author_id")).thenReturn(1L, 1L, 2L, 2L, 1L, 2L);
        when(resultSet.getString("author_name")).thenReturn("Author 1", "Author 2", "Author 1", "Author 2");
        when(resultSet.getLong("book_id")).thenReturn(101L, 101L, 102L, 102L, 103L, 103L, 104L, 104L);
        when(resultSet.getString("title")).thenReturn("Book 1", "Book 2", "Book 3", "Book 4");
        when(resultSet.getInt("published_year")).thenReturn(2020, 2021, 2022, 2023);

        return resultSet;
    }
}
