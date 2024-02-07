package org.example.dao.mapper;

import org.example.model.Author;
import org.example.model.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorMapperImpl implements AuthorMapper {

    @Override
    public Author mapRow(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getLong("author_id"));
        author.setName(resultSet.getString("name"));
        return author;
    }

    @Override
    public List<Author> mapList(ResultSet resultSet) throws SQLException {
        Map<Long, Author> authorMap = new HashMap<>();

        while (resultSet.next()) {
            Long authorId = resultSet.getLong("author_id");
            Author author = authorMap.computeIfAbsent(authorId, k -> {
                try {
                    return createAuthor(resultSet);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            });

            long bookId = resultSet.getLong("book_id");
            if (bookId > 0) {
                Book book = createBook(resultSet);
                if (!author.getBooks().contains(book)) {
                    author.getBooks().add(book);
                }
            }
        }
        return new ArrayList<>(authorMap.values());
    }

    private Author createAuthor(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getLong("author_id"));
        author.setName(resultSet.getString("author_name"));
        return author;
    }

    private Book createBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("book_id"));
        book.setTitle(resultSet.getString("title"));
        book.setPublishedYear(resultSet.getInt("published_year"));
        return book;
    }
}
