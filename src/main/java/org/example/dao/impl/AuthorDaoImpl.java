package org.example.dao.impl;

import org.example.dao.AuthorDao;
import org.example.dao.sql.SqlQuery;
import org.example.db.HikariConnectionManager;
import org.example.model.Author;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AuthorDaoImpl implements AuthorDao {

    private final DataSource dataSource;

    public AuthorDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public AuthorDaoImpl() {
        this.dataSource = HikariConnectionManager.getDataSource();
    }

    @Override
    public Author create(Author author) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     SqlQuery.INSERT_AUTHOR_BY_ID.getQuery(), Statement.RETURN_GENERATED_KEYS
             )) {
            statement.setString(1, author.getName());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    author.setId(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return author;
    }

    @Override
    public Author update(Author author) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlQuery.UPDATE_AUTHOR_BY_ID.getQuery())) {
            statement.setString(1, author.getName());
            statement.setLong(2, author.getId());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated != 1) {
                throw new SQLException("Failed to update author. Rows updated: " + rowsUpdated);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return author;
    }

    @Override
    public Author delete(Author author) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlQuery.DELETE_AUTHOR_BY_ID.getQuery())) {
            statement.setLong(1, author.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return author;
    }

    public List<Author> findAll() {
        return AuthorDao.super.findAll(dataSource);
    }

    public Author read(Long key) {
        return AuthorDao.super.read(key, dataSource);
    }
}
