package org.example.dao;

import org.example.dao.mapper.Mapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface Dao<T> {
    T create(T model);

    T read(Long key);

    T update(T model);

    T delete(T model);

    List<T> findAll();

    default T read(Long key, String findById, Mapper<T> mapper, DataSource dataSource) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(findById)) {
            statement.setLong(1, key);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapper.mapRow(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    default List<T> findAll(String findAll, Mapper<T> mapper, DataSource dataSource) {
        List<T> entities = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            entities = mapper.mapList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }
}
