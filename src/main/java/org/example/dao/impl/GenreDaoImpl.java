package org.example.dao.impl;

import org.example.dao.GenreDao;
import org.example.dao.sql.SqlQuery;
import org.example.db.HikariConnectionManager;
import org.example.model.Genre;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GenreDaoImpl implements GenreDao {

    private final DataSource dataSource;

    public GenreDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public GenreDaoImpl() {
        this.dataSource = HikariConnectionManager.getDataSource();
    }

    @Override
    public Genre create(Genre genre) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlQuery.INSERT_GENRE_BY_ID.getQuery(), Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, genre.getName());
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    genre.setId(id);  // Устанавливаем сгенерированный идентификатор в объект Genre
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlQuery.UPDATE_GENRE_BY_ID.getQuery())) {
            statement.setString(1, genre.getName());
            statement.setLong(2, genre.getId());
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated != 1) {
                throw new SQLException("Failed to update author. Rows updated: " + rowsUpdated);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genre;
    }

    @Override
    public Genre delete(Genre genre) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlQuery.DELETE_GENRE_BY_ID.getQuery())) {
            statement.setLong(1, genre.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genre;
    }

    public List<Genre> findAll() {
        return GenreDao.super.findAll(dataSource);
    }

    public Genre read(Long key) {
        return GenreDao.super.read(key, dataSource);
    }
}
