package org.example.servlet.mapper;

import org.example.model.Genre;
import org.example.servlet.dto.GenreDto;

public interface GenreDtoMapper extends DtoMapper<Genre, GenreDto> {

    @Override
    GenreDto toDto(Genre genre);

    @Override
    Genre toEntity(GenreDto genreDto);
}
