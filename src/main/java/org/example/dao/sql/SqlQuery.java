package org.example.dao.sql;

public enum SqlQuery {
    SELECT_ALL_BOOKS("""
            SELECT b.book_id, b.title, b.published_year, b.author_id, a.name AS author_name, g.genre_id, g.name 
            FROM books b 
            LEFT JOIN authors a on a.author_id = b.author_id 
            LEFT JOIN bookgenres bg on b.book_id = bg.book_id 
            LEFT JOIN genres g on g.genre_id = bg.genre_id
            """),
    SELECT_ALL_AUTHORS("""
                        SELECT a.author_id, a.name AS author_name, b.book_id, b.title, b.published_year
                        FROM authors a
                        LEFT JOIN books b on a.author_id = b.author_id
            """),
    SELECT_ALL_GENRES("""
            SELECT g.genre_id, g.name, b.book_id, b.title, b.published_year, b.author_id, a.name AS author_name 
            FROM genres g 
            LEFT JOIN bookgenres bg on g.genre_id = bg.genre_id 
            LEFT JOIN books b on b.book_id = bg.book_id 
            LEFT JOIN authors a on a.author_id = b.author_id
                """),

    SELECT_AUTHOR_BY_ID("""
            SELECT author_id, name
            FROM authors WHERE author_id = ?
            """),
    SELECT_BOOK_BY_ID("""
            SELECT book_id, title, published_year, author_id
            FROM books
            WHERE book_id = ?
            """),
    SELECT_GENRE_BY_ID("""
            SELECT genre_id, name FROM genres WHERE genre_id = ?
            """),
    INSERT_AUTHOR_BY_ID("""
            INSERT INTO authors (author_id, name) VALUES (DEFAULT, ?)
            """),
    INSERT_BOOK_BY_ID("""
            INSERT INTO books (book_id, title, published_year, author_id) VALUES (DEFAULT, ?,?,?)
            """),
    INSERT_GENRE_BY_ID("""
            INSERT INTO genres (genre_id, name) VALUES (DEFAULT, ?)
            """),
    DELETE_AUTHOR_BY_ID("""
            DELETE FROM authors WHERE author_id = ?
            """),
    DELETE_BOOK_BY_ID("""
            DELETE FROM books WHERE book_id = ?
            """),
    DELETE_GENRE_BY_ID("""
            DELETE FROM genres WHERE genre_id = ?
            """),
    UPDATE_AUTHOR_BY_ID("""
            UPDATE authors SET name = ? WHERE author_id = ?
            """),
    UPDATE_BOOK_BY_ID("""
            UPDATE books SET title = ?, published_year = ? WHERE book_id = ?
            """),
    UPDATE_GENRE_BY_ID("""
            UPDATE genres SET name = ? WHERE genre_id = ?
            """);


    private final String query;

    SqlQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
