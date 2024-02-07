package org.example.dao;

import org.example.dao.mapper.GenreMapper;
import org.example.dao.mapper.GenreMapperImpl;
import org.example.dao.sql.SqlQuery;
import org.example.model.Genre;

import javax.sql.DataSource;
import java.util.List;

public interface GenreDao extends Dao<Genre> {

    private GenreMapper getMapper() {
        return new GenreMapperImpl();
    }

    default List<Genre> findAll(DataSource dataSource) {
        return Dao.super.findAll(SqlQuery.SELECT_ALL_GENRES.getQuery(), getMapper(), dataSource);
    }

    default Genre read(Long key, DataSource dataSource) {
        return Dao.super.read(key, SqlQuery.SELECT_GENRE_BY_ID.getQuery(), getMapper(), dataSource);
    }
}
