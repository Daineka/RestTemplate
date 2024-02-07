package org.example.servlet.mapper;

import org.example.model.Author;
import org.example.model.Book;
import org.example.servlet.dto.AuthorDto;
import org.example.servlet.dto.BookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthorDtoMapperImplTest {

    private AuthorDtoMapperImpl authorDtoMapper;

    @BeforeEach
    void setUp() {
        authorDtoMapper = new AuthorDtoMapperImpl();
    }

    @Test
    void testToDto_withValidAuthor() {
        Author author = new Author();
        author.setId(1L);
        author.setName("John Doe");

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setPublishedYear(2020);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setPublishedYear(2021);

        Set<Book> books = new HashSet<>();
        books.add(book1);
        books.add(book2);

        author.setBooks(books);

        AuthorDto authorDto = authorDtoMapper.toDto(author);

        assertEquals(author.getId(), authorDto.getId());
        assertEquals(author.getName(), authorDto.getName());
        assertEquals(2, authorDto.getBooks().size());
    }

    @Test
    void testToDto_withNullAuthor() {
        Author author = null;

        AuthorDto authorDto = authorDtoMapper.toDto(author);

        assertNull(authorDto);
    }

    @Test
    void testToEntity_withValidAuthorDto() {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setName("John Doe");

        BookDto bookDto1 = new BookDto();
        bookDto1.setId(1L);
        bookDto1.setTitle("Book 1");
        bookDto1.setPublishedYear(2020);

        BookDto bookDto2 = new BookDto();
        bookDto2.setId(2L);
        bookDto2.setTitle("Book 2");
        bookDto2.setPublishedYear(2021);

        Set<BookDto> bookDtos = new HashSet<>();
        bookDtos.add(bookDto1);
        bookDtos.add(bookDto2);

        authorDto.setBooks(bookDtos);

        Author author = authorDtoMapper.toEntity(authorDto);

        assertEquals(authorDto.getId(), author.getId());
        assertEquals(authorDto.getName(), author.getName());
        assertEquals(2, author.getBooks().size());
    }

    @Test
    void testToEntity_withNullAuthorDto() {
        AuthorDto authorDto = null;

        Author author = authorDtoMapper.toEntity(authorDto);

        assertNull(author);
    }
}
