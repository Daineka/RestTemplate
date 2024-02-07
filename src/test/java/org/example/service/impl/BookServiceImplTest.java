package org.example.service.impl;

import org.example.dao.BookDao;
import org.example.dao.impl.AuthorDaoImpl;
import org.example.dao.impl.BookDaoImpl;
import org.example.model.Author;
import org.example.model.Book;
import org.example.service.BookService;
import org.example.servlet.dto.AuthorDto;
import org.example.servlet.dto.BookDto;
import org.example.servlet.mapper.AuthorDtoMapperImpl;
import org.example.servlet.mapper.BookDtoMapper;
import org.example.servlet.mapper.BookDtoMapperImpl;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookServiceImplTest {

    @Mock
    private BookDao bookDaoMock;
    @Mock
    private BookDtoMapper dtoMapperMock;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookServiceImpl(bookDaoMock, dtoMapperMock);
    }

    @Test
    void testConstructor() {
        BookDaoImpl bookDaoMock = mock(BookDaoImpl.class);
        BookDtoMapperImpl dtoMapperMock = mock(BookDtoMapperImpl.class);

        BookServiceImpl bookService = new BookServiceImpl(bookDaoMock, dtoMapperMock);

        assertNotNull(bookService);
    }

    @Test
    void testGetAll() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book1", 2000, new Author(1L, "Name1")));
        books.add(new Book(2L, "Book2", 2020, new Author(2L, "Name2")));

        List<BookDto> expectedDtoList = new ArrayList<>();
        expectedDtoList.add(new BookDto(1L, "Book1", 2000, new AuthorDto(1L, "Name1")));
        expectedDtoList.add(new BookDto(2L, "Book2", 2020, new AuthorDto(2L, "Name2")));

        when(bookDaoMock.findAll()).thenReturn(books);
        when(dtoMapperMock.toDto(any())).thenReturn(new BookDto(1L, "Book1", 2000, new AuthorDto(1L, "Name1")), new BookDto(2L, "Book2", 2020, new AuthorDto(2L, "Name2")));

        // Act
        List<BookDto> result = bookService.getAll();

        // Assert
        assertEquals(expectedDtoList, result);
        verify(bookDaoMock, times(1)).findAll();
        verify(dtoMapperMock, times(2)).toDto(any());
    }

    @Test
    void testSave() {
        Book book = new Book(1L, "Book1", 2000, new Author(1L, "Name1"));
        BookDto bookDto = new BookDto(1L, "Book1", 2020, new AuthorDto(1L, "Name1"));

        when(dtoMapperMock.toEntity(bookDto)).thenReturn(book);
        when(bookDaoMock.create(book)).thenReturn(book);
        when(dtoMapperMock.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.save(bookDto);

        assertEquals(bookDto, result);
        verify(dtoMapperMock, times(1)).toEntity(bookDto);
        verify(bookDaoMock, times(1)).create(book);
        verify(dtoMapperMock, times(1)).toDto(book);
    }

    @Test
    void testGet() {
        long bookId = 1L;
        Book book = new Book(bookId, "Book1", 2000, new Author(1L, "Name1"));
        BookDto expectedDto = new BookDto(bookId, "Book1", 2020, new AuthorDto(1L, "Name1"));

        when(bookDaoMock.read(bookId)).thenReturn(book);
        when(dtoMapperMock.toDto(book)).thenReturn(expectedDto);

        BookDto result = bookService.get(bookId);

        assertEquals(expectedDto, result);
        verify(bookDaoMock, times(1)).read(bookId);
        verify(dtoMapperMock, times(1)).toDto(book);
    }

    @Test
    void testUpdate() {
        BookDto bookDto = new BookDto(1L, "Updated Book", 2010, new AuthorDto(1L, "Name1"));
        Book book = new Book(1L, "Updated Book", 2010, new Author(1L, "Name1"));

        when(dtoMapperMock.toEntity(bookDto)).thenReturn(book);
        when(bookDaoMock.update(book)).thenReturn(book);
        when(dtoMapperMock.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.update(bookDto);

        assertEquals(bookDto, result);
        verify(dtoMapperMock, times(1)).toEntity(bookDto);
        verify(bookDaoMock, times(1)).update(book);
        verify(dtoMapperMock, times(1)).toDto(book);
    }

    @Test
    void testDelete() {
        BookDto bookDto = new BookDto(1L, "To be deleted", 2020, new AuthorDto(1L, "Name1"));
        Book book = new Book(1L, "To be deleted", 2020, new Author(1L, "Name1"));

        when(dtoMapperMock.toEntity(bookDto)).thenReturn(book);
        when(bookDaoMock.delete(book)).thenReturn(book);
        when(dtoMapperMock.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.delete(bookDto);

        assertEquals(bookDto, result);
        verify(dtoMapperMock, times(1)).toEntity(bookDto);
        verify(bookDaoMock, times(1)).delete(book);
        verify(dtoMapperMock, times(1)).toDto(book);
    }
}
