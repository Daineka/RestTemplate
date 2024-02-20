package org.example.dao.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SqlQueryTest {

    @Test
    void testSqlQueries() {
        assertEquals("""
                SELECT b.book_id, b.title, b.published_year, b.author_id, a.name AS author_name, g.genre_id, g.name 
                FROM books b 
                LEFT JOIN authors a on a.author_id = b.author_id 
                LEFT JOIN bookgenres bg on b.book_id = bg.book_id 
                LEFT JOIN genres g on g.genre_id = bg.genre_id
                """, SqlQuery.SELECT_ALL_BOOKS.getQuery());

        assertEquals("""
                            SELECT a.author_id, a.name AS author_name, b.book_id, b.title, b.published_year
                            FROM authors a
                            LEFT JOIN books b on a.author_id = b.author_id
                """, SqlQuery.SELECT_ALL_AUTHORS.getQuery());

        assertEquals("""
                SELECT g.genre_id, g.name, b.book_id, b.title, b.published_year, b.author_id, a.name AS author_name 
                FROM genres g 
                LEFT JOIN bookgenres bg on g.genre_id = bg.genre_id 
                LEFT JOIN books b on b.book_id = bg.book_id 
                LEFT JOIN authors a on a.author_id = b.author_id
                """, SqlQuery.SELECT_ALL_GENRES.getQuery());

        assertEquals("""
                SELECT author_id, name
                FROM authors WHERE author_id = ?
                """, SqlQuery.SELECT_AUTHOR_BY_ID.getQuery());

        assertEquals("""
                SELECT book_id, title, published_year, author_id
                FROM books
                WHERE book_id = ?
                """, SqlQuery.SELECT_BOOK_BY_ID.getQuery());

        assertEquals("""
                SELECT genre_id, name FROM genres WHERE genre_id = ?
                """, SqlQuery.SELECT_GENRE_BY_ID.getQuery());

        assertEquals("""
                INSERT INTO authors (author_id, name) VALUES (DEFAULT, ?)
                """, SqlQuery.INSERT_AUTHOR_BY_ID.getQuery());

        assertEquals("""
                INSERT INTO books (book_id, title, published_year, author_id) VALUES (DEFAULT, ?,?,?)
                """, SqlQuery.INSERT_BOOK_BY_ID.getQuery());

        assertEquals("""
                INSERT INTO genres (genre_id, name) VALUES (DEFAULT, ?)
                """, SqlQuery.INSERT_GENRE_BY_ID.getQuery());

        assertEquals("""
                DELETE FROM authors WHERE author_id = ?
                """, SqlQuery.DELETE_AUTHOR_BY_ID.getQuery());

        assertEquals("""
                DELETE FROM books WHERE book_id = ?
                """, SqlQuery.DELETE_BOOK_BY_ID.getQuery());

        assertEquals("""
                DELETE FROM genres WHERE genre_id = ?
                """, SqlQuery.DELETE_GENRE_BY_ID.getQuery());

        assertEquals("""
                UPDATE authors SET name = ? WHERE author_id = ?
                """, SqlQuery.UPDATE_AUTHOR_BY_ID.getQuery());

        assertEquals("""
                UPDATE books SET title = ?, published_year = ? WHERE book_id = ?
                """, SqlQuery.UPDATE_BOOK_BY_ID.getQuery());

        assertEquals("""
                UPDATE genres SET name = ? WHERE genre_id = ?
                """, SqlQuery.UPDATE_GENRE_BY_ID.getQuery());
    }
}
