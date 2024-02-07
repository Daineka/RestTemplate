package org.example.dao;

import org.example.dao.mapper.BookMapper;
import org.example.dao.mapper.BookMapperImpl;
import org.example.dao.sql.SqlQuery;
import org.example.model.Book;

import javax.sql.DataSource;
import java.util.List;

public interface BookDao extends Dao<Book> {
    private BookMapper getMapper() {
        return new BookMapperImpl();
    }

    default List<Book> findAll(DataSource dataSource) {
        return Dao.super.findAll(SqlQuery.SELECT_ALL_BOOKS.getQuery(), getMapper(), dataSource);
    }

    default Book read(Long key, DataSource dataSource) {
        return Dao.super.read(key, SqlQuery.SELECT_BOOK_BY_ID.getQuery(), getMapper(), dataSource);
    }
}
