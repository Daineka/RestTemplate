package org.example.dao;

import org.example.dao.mapper.AuthorMapperImpl;
import org.example.dao.mapper.Mapper;
import org.example.dao.sql.SqlQuery;
import org.example.model.Author;

import javax.sql.DataSource;
import java.util.List;

public interface AuthorDao extends Dao<Author> {
    default Author read(Long key, DataSource dataSource) {
        return Dao.super.read(key, SqlQuery.SELECT_AUTHOR_BY_ID.getQuery(), new AuthorMapperImpl(), dataSource);
    }

    default List<Author> findAll(DataSource dataSource) {
        return Dao.super.findAll(SqlQuery.SELECT_ALL_AUTHORS.getQuery(), new AuthorMapperImpl(), dataSource);
    }
}
