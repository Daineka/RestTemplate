package org.example.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Mapper<T> {
    T mapRow(ResultSet resultSet) throws SQLException;

    List<T> mapList(ResultSet resultSet) throws SQLException;

}
