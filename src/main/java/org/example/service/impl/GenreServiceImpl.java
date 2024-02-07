package org.example.service.impl;

import org.example.dao.GenreDao;
import org.example.dao.impl.BookDaoImpl;
import org.example.dao.impl.GenreDaoImpl;
import org.example.service.GenreService;
import org.example.servlet.dto.GenreDto;
import org.example.servlet.mapper.BookDtoMapperImpl;
import org.example.servlet.mapper.GenreDtoMapper;
import org.example.servlet.mapper.GenreDtoMapperImpl;

import java.util.List;

public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;
    private final GenreDtoMapper dtoMapper;

    public GenreServiceImpl(GenreDao genreDao, GenreDtoMapper dtoMapper) {
        this.genreDao = genreDao;
        this.dtoMapper = dtoMapper;
    }

    public GenreServiceImpl() {
        this.genreDao = new GenreDaoImpl();
        this.dtoMapper = new GenreDtoMapperImpl();
    }

    @Override
    public List<GenreDto> getAll() {
        return genreDao.findAll().stream()
                .map(dtoMapper::toDto)
                .toList();
    }

    @Override
    public GenreDto save(GenreDto genreDto) {
        return dtoMapper.toDto(
                genreDao.create(dtoMapper.toEntity(genreDto))
        );
    }

    @Override
    public GenreDto get(Long key) {
        return dtoMapper.toDto(
                genreDao.read(key)
        );
    }

    @Override
    public GenreDto update(GenreDto genreDto) {
        return dtoMapper.toDto(
                genreDao.update(dtoMapper.toEntity(genreDto))
        );
    }

    @Override
    public GenreDto delete(GenreDto genreDto) {
        return dtoMapper.toDto(
                genreDao.delete(dtoMapper.toEntity(genreDto))
        );
    }
}
