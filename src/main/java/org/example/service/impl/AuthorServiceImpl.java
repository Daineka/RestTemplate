package org.example.service.impl;

import org.example.dao.AuthorDao;
import org.example.dao.impl.AuthorDaoImpl;
import org.example.service.AuthorService;
import org.example.servlet.dto.AuthorDto;
import org.example.servlet.mapper.AuthorDtoMapper;
import org.example.servlet.mapper.AuthorDtoMapperImpl;

import java.util.List;

public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;
    private final AuthorDtoMapper dtoMapper;

    public AuthorServiceImpl(AuthorDao authorDao, AuthorDtoMapper dtoMapper) {
        this.authorDao = authorDao;
        this.dtoMapper = dtoMapper;
    }

    public AuthorServiceImpl() {
        this.authorDao = new AuthorDaoImpl();
        this.dtoMapper = new AuthorDtoMapperImpl();
    }

    @Override
    public List<AuthorDto> getAll() {
        return authorDao.findAll().stream()
                .map(dtoMapper::toDto)
                .toList();
    }

    @Override
    public AuthorDto save(AuthorDto authorDto) {
        return dtoMapper.toDto(
                authorDao.create(dtoMapper.toEntity(authorDto))
        );
    }

    @Override
    public AuthorDto get(Long key) {
        return dtoMapper.toDto(
                authorDao.read(key)
        );
    }

    @Override
    public AuthorDto update(AuthorDto authorDto) {
        return dtoMapper.toDto(
                authorDao.update(dtoMapper.toEntity(authorDto))
        );
    }

    @Override
    public AuthorDto delete(AuthorDto authorDto) {
        return dtoMapper.toDto(
                authorDao.delete(dtoMapper.toEntity(authorDto))
        );
    }
}
