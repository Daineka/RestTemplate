package org.example.dao.mapper;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookMapperImpl implements BookMapper {

    @Override
    public Book mapRow(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("book_id"));
        book.setTitle(resultSet.getString("title"));
        book.setPublishedYear(resultSet.getInt("published_year"));

        long authorId = resultSet.getLong("author_id");
        if (authorId > 0) {
            Author author = new Author();
            author.setId(authorId);
            book.setAuthor(author);
        }
        return book;
    }

    @Override
    public List<Book> mapList(ResultSet resultSet) throws SQLException {
        Map<Long, Book> bookMap = new HashMap<>();

        while (resultSet.next()) {
            Long bookId = resultSet.getLong("book_id");

            Book book = bookMap.computeIfAbsent(bookId, k -> {
                try {
                    return createBook(resultSet);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            });

            long authorId = resultSet.getLong("author_id");
            if (authorId > 0) {
                Author author = new Author();
                author.setId(authorId);
                author.setName(resultSet.getString("author_name"));
                book.setAuthor(author);
            }

            long genreId = resultSet.getLong("genre_id");
            if (genreId > 0) {
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(resultSet.getString("name"));
                book.getGenres().add(genre);
            }
        }

        return new ArrayList<>(bookMap.values());
    }

    private Book createBook(ResultSet resultSet) throws SQLException {
        Book newBook = new Book();
        newBook.setId(resultSet.getLong("book_id"));
        newBook.setTitle(resultSet.getString("title"));
        newBook.setPublishedYear(resultSet.getInt("published_year"));
        return newBook;
    }
}
