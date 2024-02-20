package org.example.dao.impl;

import org.example.dao.BookDao;
import org.example.dao.sql.SqlQuery;
import org.example.db.HikariConnectionManager;
import org.example.model.Book;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class BookDaoImpl implements BookDao {

    private final DataSource dataSource;

    public BookDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public BookDaoImpl() {
        this.dataSource = HikariConnectionManager.getDataSource();
    }

    @Override
    public Book create(Book book) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlQuery.INSERT_BOOK_BY_ID.getQuery(), Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getTitle());
            statement.setInt(2, book.getPublishedYear());
            statement.setLong(3, book.getAuthor().getId());
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    book.setId(id);  // Устанавливаем сгенерированный идентификатор в объект Book
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public Book update(Book book) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlQuery.UPDATE_BOOK_BY_ID.getQuery())) {
            statement.setString(1, book.getTitle());
            statement.setInt(2, book.getPublishedYear());
            statement.setLong(3, book.getId());
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated != 1) {
                throw new SQLException("Failed to update book. Rows updated: " + rowsUpdated);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public Book delete(Book book) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlQuery.DELETE_BOOK_BY_ID.getQuery())) {
            statement.setLong(1, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    public List<Book> findAll() {
        return BookDao.super.findAll(dataSource);
    }

    public Book read(Long key) {
        return BookDao.super.read(key, dataSource);
    }
}
