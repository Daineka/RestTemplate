package org.example.servlet.mapper;

import org.example.model.Author;
import org.example.servlet.dto.AuthorDto;

public interface AuthorDtoMapper extends DtoMapper<Author, AuthorDto> {

    @Override
    AuthorDto toDto(Author author);

    @Override
    Author toEntity(AuthorDto authorDto);
}

