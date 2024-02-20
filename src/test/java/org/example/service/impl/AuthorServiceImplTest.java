package org.example.service.impl;

import org.example.dao.AuthorDao;
import org.example.dao.impl.AuthorDaoImpl;
import org.example.model.Author;
import org.example.service.AuthorService;
import org.example.servlet.dto.AuthorDto;
import org.example.servlet.mapper.AuthorDtoMapper;
import org.example.servlet.mapper.AuthorDtoMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthorServiceImplTest {

    @Mock
    private AuthorDao authorDaoMock;
    @Mock
    private AuthorDtoMapper dtoMapperMock;
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authorService = new AuthorServiceImpl(authorDaoMock, dtoMapperMock);
    }

    @Test
    void testConstructor() {
        AuthorDaoImpl authorDaoMock = mock(AuthorDaoImpl.class);
        AuthorDtoMapperImpl dtoMapperMock = mock(AuthorDtoMapperImpl.class);

        AuthorServiceImpl authorService = new AuthorServiceImpl(authorDaoMock, dtoMapperMock);

        assertNotNull(authorService);
    }

    @Test
    void testGetAll() {
        List<Author> expectedEntities = new ArrayList<>();
        expectedEntities.add(new Author());
        expectedEntities.add(new Author());

        List<AuthorDto> expectedDtoList = new ArrayList<>();
        expectedDtoList.add(new AuthorDto());
        expectedDtoList.add(new AuthorDto());

        when(authorDaoMock.findAll()).thenReturn(expectedEntities);
        when(dtoMapperMock.toDto(any())).thenReturn(new AuthorDto());

        // Act
        List<AuthorDto> result = authorService.getAll();

        // Assert
        assertEquals(expectedDtoList, result);
        verify(authorDaoMock, times(1)).findAll();
        verify(dtoMapperMock, times(2)).toDto(any());
    }

    @Test
    void testGetAllWithEmptyList() {
        // Arrange
        when(authorDaoMock.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<AuthorDto> result = authorService.getAll();

        // Assert
        assertEquals(new ArrayList<>(), result);
        verify(authorDaoMock, times(1)).findAll();
        verify(dtoMapperMock, never()).toDto(any());
    }

    @Test
    void testSave() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Hello");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setName("Hello");

        when(dtoMapperMock.toEntity(authorDto)).thenReturn(author);
        when(authorDaoMock.create(author)).thenReturn(author);
        when(dtoMapperMock.toDto(author)).thenReturn(authorDto);

        AuthorDto result = authorService.save(authorDto);

        assertEquals(authorDto, result);
        verify(dtoMapperMock, times(1)).toEntity(authorDto);
        verify(authorDaoMock, times(1)).create(author);
        verify(dtoMapperMock, times(1)).toDto(author);
    }

    @Test
    void testGet() {
        long authorId = 1L;
        Author author = new Author();
        author.setId(authorId);
        author.setName("Test Author");
        AuthorDto expectedDto = new AuthorDto();
        expectedDto.setId(authorId);
        expectedDto.setName("Test Author");

        when(authorDaoMock.read(authorId)).thenReturn(author);
        when(dtoMapperMock.toDto(author)).thenReturn(expectedDto);

        AuthorDto result = authorService.get(authorId);

        assertEquals(expectedDto, result);
        verify(authorDaoMock, times(1)).read(authorId);
        verify(dtoMapperMock, times(1)).toDto(author);
    }


    @Test
    void testUpdate() {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setName("Updated Author");

        Author author = new Author();
        author.setId(1L);
        author.setName("Updated Author");

        when(dtoMapperMock.toEntity(authorDto)).thenReturn(author);
        when(authorDaoMock.update(author)).thenReturn(author);
        when(dtoMapperMock.toDto(author)).thenReturn(authorDto);

        AuthorDto result = authorService.update(authorDto);

        assertEquals(authorDto, result);
        verify(dtoMapperMock, times(1)).toEntity(authorDto);
        verify(authorDaoMock, times(1)).update(author);
        verify(dtoMapperMock, times(1)).toDto(author);
    }

    @Test
    void testDelete() {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setName("To be deleted");

        Author author = new Author();
        author.setId(1L);
        author.setName("To be deleted");

        when(dtoMapperMock.toEntity(authorDto)).thenReturn(author);
        when(authorDaoMock.delete(author)).thenReturn(author);
        when(dtoMapperMock.toDto(author)).thenReturn(authorDto);

        AuthorDto result = authorService.delete(authorDto);

        assertEquals(authorDto, result);
        verify(dtoMapperMock, times(1)).toEntity(authorDto);
        verify(authorDaoMock, times(1)).delete(author);
        verify(dtoMapperMock, times(1)).toDto(author);
    }
}
