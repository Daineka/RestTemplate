package org.example.servlet.mapper;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.example.servlet.dto.AuthorDto;
import org.example.servlet.dto.BookDto;
import org.example.servlet.dto.GenreDto;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookDtoMapperImplTest {

    @Test
    void testToDto_withValidBook() {
        BookDtoMapper mapper = new BookDtoMapperImpl();
        Author author = new Author(1L, "John Doe");
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1L, "Fantasy"));
        genres.add(new Genre(2L, "Adventure"));
        Book book = new Book(1L, "Sample Book", 2022, author, genres);

        BookDto bookDto = mapper.toDto(book);

        assertEquals(book.getId(), bookDto.getId());
        assertEquals(book.getTitle(), bookDto.getTitle());
        assertEquals(book.getPublishedYear(), bookDto.getPublishedYear());
        assertEquals(book.getAuthor().getId(), bookDto.getAuthor().getId());
        assertEquals(book.getAuthor().getName(), bookDto.getAuthor().getName());
        assertEquals(book.getGenres().size(), bookDto.getGenres().size());
        for (Genre genre : book.getGenres()) {
            assertTrue(bookDto.getGenres().stream()
                    .anyMatch(genreDto -> Objects.equals(genre.getId(), genreDto.getId()) && genre.getName().equals(genreDto.getName())));
        }
    }

    @Test
    void testToDto_withNullBook() {
        BookDtoMapper mapper = new BookDtoMapperImpl();

        BookDto bookDto = mapper.toDto(null);

        assertNull(bookDto);
    }

    @Test
    void testToEntity_withValidBookDto() {
        BookDtoMapper mapper = new BookDtoMapperImpl();
        AuthorDto authorDto = new AuthorDto(1L, "John Doe");
        Set<GenreDto> genreDtos = new HashSet<>();
        genreDtos.add(new GenreDto(1L, "Fantasy"));
        genreDtos.add(new GenreDto(2L, "Adventure"));
        BookDto bookDto = new BookDto(1L, "Sample Book", 2022, authorDto, genreDtos);

        Book book = mapper.toEntity(bookDto);

        assertEquals(bookDto.getId(), book.getId());
        assertEquals(bookDto.getTitle(), book.getTitle());
        assertEquals(bookDto.getPublishedYear(), book.getPublishedYear());
        assertEquals(bookDto.getAuthor().getId(), book.getAuthor().getId());
        assertEquals(bookDto.getAuthor().getName(), book.getAuthor().getName());
        assertEquals(bookDto.getGenres().size(), book.getGenres().size());
        for (GenreDto genreDto : bookDto.getGenres()) {
            assertTrue(book.getGenres().stream()
                    .anyMatch(genre -> Objects.equals(genreDto.getId(), genre.getId()) && genreDto.getName().equals(genre.getName())));
        }
    }

    @Test
    void testToEntity_withNullBookDto() {
        BookDtoMapper mapper = new BookDtoMapperImpl();

        Book book = mapper.toEntity(null);

        assertNull(book);
    }
}
