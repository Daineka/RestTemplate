package org.example.service.impl;

import org.example.dao.GenreDao;
import org.example.dao.impl.BookDaoImpl;
import org.example.dao.impl.GenreDaoImpl;
import org.example.model.Genre;
import org.example.servlet.dto.GenreDto;
import org.example.servlet.mapper.BookDtoMapperImpl;
import org.example.servlet.mapper.GenreDtoMapper;
import org.example.servlet.mapper.GenreDtoMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class GenreServiceImplTest {

    @Mock
    private GenreDao genreDaoMock;
    @Mock
    private GenreDtoMapper dtoMapperMock;
    private GenreServiceImpl genreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        genreService = new GenreServiceImpl(genreDaoMock, dtoMapperMock);
    }

    @Test
    void testConstructor() {
        GenreDaoImpl genreDaoMock = mock(GenreDaoImpl.class);
        GenreDtoMapperImpl dtoMapperMock = mock(GenreDtoMapperImpl.class);

        GenreServiceImpl genreService = new GenreServiceImpl(genreDaoMock, dtoMapperMock);

        assertNotNull(genreService);
    }

    @Test
    void testGetAll() {
        List<GenreDto> expectedDtoList = new ArrayList<>();
        when(genreDaoMock.findAll()).thenReturn(new ArrayList<>());
        when(dtoMapperMock.toDto(any())).thenReturn(new GenreDto());

        List<GenreDto> result = genreService.getAll();

        assertEquals(expectedDtoList, result);
        verify(genreDaoMock, times(1)).findAll();
        verify(dtoMapperMock, never()).toDto(any());
    }

    @Test
    void testSave() {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(1L);
        genreDto.setName("Fiction");

        when(dtoMapperMock.toEntity(genreDto)).thenReturn(new Genre());
        when(genreDaoMock.create(any())).thenReturn(new Genre());
        when(dtoMapperMock.toDto(any())).thenReturn(genreDto);

        GenreDto result = genreService.save(genreDto);

        assertEquals(genreDto, result);
        verify(dtoMapperMock, times(1)).toEntity(genreDto);
        verify(genreDaoMock, times(1)).create(any());
        verify(dtoMapperMock, times(1)).toDto(any());
    }

    @Test
    void testGet() {
        long genreId = 1L;
        Genre genre = new Genre();
        genre.setId(genreId);
        genre.setName("Fiction");
        GenreDto expectedDto = new GenreDto();
        expectedDto.setId(genreId);
        expectedDto.setName("Fiction");

        when(genreDaoMock.read(genreId)).thenReturn(genre);
        when(dtoMapperMock.toDto(genre)).thenReturn(expectedDto);

        GenreDto result = genreService.get(genreId);

        assertEquals(expectedDto, result);
        verify(genreDaoMock, times(1)).read(genreId);
        verify(dtoMapperMock, times(1)).toDto(genre);
    }

    @Test
    void testUpdate() {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(1L);
        genreDto.setName("Updated Genre");

        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Updated Genre");

        when(dtoMapperMock.toEntity(genreDto)).thenReturn(genre);
        when(genreDaoMock.update(genre)).thenReturn(genre);
        when(dtoMapperMock.toDto(genre)).thenReturn(genreDto);

        GenreDto result = genreService.update(genreDto);

        assertEquals(genreDto, result);
        verify(dtoMapperMock, times(1)).toEntity(genreDto);
        verify(genreDaoMock, times(1)).update(genre);
        verify(dtoMapperMock, times(1)).toDto(genre);
    }

    @Test
    void testDelete() {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(1L);
        genreDto.setName("To be deleted");

        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("To be deleted");

        when(dtoMapperMock.toEntity(genreDto)).thenReturn(genre);
        when(genreDaoMock.delete(genre)).thenReturn(genre);
        when(dtoMapperMock.toDto(genre)).thenReturn(genreDto);

        GenreDto result = genreService.delete(genreDto);

        assertEquals(genreDto, result);
        verify(dtoMapperMock, times(1)).toEntity(genreDto);
        verify(genreDaoMock, times(1)).delete(genre);
        verify(dtoMapperMock, times(1)).toDto(genre);
    }
}
