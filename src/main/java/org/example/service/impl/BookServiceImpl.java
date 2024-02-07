package org.example.service.impl;

import org.example.dao.BookDao;
import org.example.dao.impl.BookDaoImpl;
import org.example.service.BookService;
import org.example.servlet.dto.BookDto;
import org.example.servlet.mapper.BookDtoMapper;
import org.example.servlet.mapper.BookDtoMapperImpl;

import java.util.List;

public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final BookDtoMapper dtoMapper;

    public BookServiceImpl(BookDao bookDao, BookDtoMapper dtoMapper) {
        this.bookDao = bookDao;
        this.dtoMapper = dtoMapper;
    }

    public BookServiceImpl() {
        this.bookDao = new BookDaoImpl();
        this.dtoMapper = new BookDtoMapperImpl();
    }

    @Override
    public List<BookDto> getAll() {
        return bookDao.findAll().stream()
                .map(dtoMapper::toDto)
                .toList();
    }

    @Override
    public BookDto save(BookDto bookDto) {
        return dtoMapper.toDto(
                bookDao.create(dtoMapper.toEntity(bookDto))
        );
    }

    @Override
    public BookDto get(Long key) {
        return dtoMapper.toDto(
                bookDao.read(key)
        );
    }

    @Override
    public BookDto update(BookDto bookDto) {
        return dtoMapper.toDto(
                bookDao.update(dtoMapper.toEntity(bookDto))
        );
    }

    @Override
    public BookDto delete(BookDto bookDto) {
        return dtoMapper.toDto(
                bookDao.delete(dtoMapper.toEntity(bookDto))
        );
    }
}
