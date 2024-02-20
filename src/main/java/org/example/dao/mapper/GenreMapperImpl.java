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

public class GenreMapperImpl implements GenreMapper {

    @Override
    public Genre mapRow(ResultSet resultSet) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getLong("genre_id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    }

    @Override
    public List<Genre> mapList(ResultSet resultSet) throws SQLException {
        Map<Long, Genre> genreMap = new HashMap<>();

        while (resultSet.next()) {
            Long genreId = resultSet.getLong("genre_id");

            Genre genre = genreMap.computeIfAbsent(genreId, k -> {
                try {
                    return createGenre(resultSet);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            });

            if (resultSet.getLong("book_id") > 0) {
                Book book = createBook(resultSet);
                genre.getBooks().add(book);
            }
        }

        return new ArrayList<>(genreMap.values());
    }

    private Genre createGenre(ResultSet resultSet) throws SQLException {
        Genre newGenre = new Genre();
        newGenre.setId(resultSet.getLong("genre_id"));
        newGenre.setName(resultSet.getString("name"));
        return newGenre;
    }

    private Book createBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("book_id"));
        book.setTitle(resultSet.getString("title"));
        book.setPublishedYear(resultSet.getInt("published_year"));

        Author author = new Author();
        author.setId(resultSet.getLong("author_id"));
        author.setName(resultSet.getString("author_name"));

        book.setAuthor(author);

        return book;
    }
}
