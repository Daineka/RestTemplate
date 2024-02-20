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

class GenreDtoMapperImplTest {

    @Test
    void testToDto_withValidGenre() {
        GenreDtoMapper mapper = new GenreDtoMapperImpl();
        Set<Book> books = new HashSet<>();
        books.add(new Book(1L, "Sample Book 1", 2022, new Author(1L, "John Doe"), null));
        books.add(new Book(2L, "Sample Book 2", 2023, new Author(2L, "Jane Smith"), null));
        Genre genre = new Genre(1L, "Fantasy", books);

        GenreDto genreDto = mapper.toDto(genre);

        assertEquals(genre.getId(), genreDto.getId());
        assertEquals(genre.getName(), genreDto.getName());
        assertEquals(genre.getBooks().size(), genreDto.getBooks().size());
        for (Book book : genre.getBooks()) {
            assertTrue(genreDto.getBooks().stream()
                    .anyMatch(bookDto -> Objects.equals(book.getId(), bookDto.getId()) && book.getTitle().equals(bookDto.getTitle())));
        }
    }

    @Test
    void testToDto_withNullGenre() {
        GenreDtoMapper mapper = new GenreDtoMapperImpl();

        GenreDto genreDto = mapper.toDto(null);

        assertNull(genreDto);
    }

    @Test
    void testToEntity_withValidGenreDto() {
        GenreDtoMapper mapper = new GenreDtoMapperImpl();
        Set<BookDto> bookDtos = new HashSet<>();
        bookDtos.add(new BookDto(1L, "Sample Book 1", 2022, new AuthorDto(1L, "John Doe"), null));
        bookDtos.add(new BookDto(2L, "Sample Book 2", 2023, new AuthorDto(2L, "Jane Smith"), null));
        GenreDto genreDto = new GenreDto(1L, "Fantasy", bookDtos);

        Genre genre = mapper.toEntity(genreDto);

        assertEquals(genreDto.getId(), genre.getId());
        assertEquals(genreDto.getName(), genre.getName());
        assertEquals(genreDto.getBooks().size(), genre.getBooks().size());
        for (BookDto bookDto : genreDto.getBooks()) {
            assertTrue(genre.getBooks().stream()
                    .anyMatch(book -> Objects.equals(bookDto.getId(), book.getId()) && bookDto.getTitle().equals(book.getTitle())));
        }
    }

    @Test
    void testToEntity_withNullGenreDto() {
        GenreDtoMapper mapper = new GenreDtoMapperImpl();

        Genre genre = mapper.toEntity(null);

        assertNull(genre);
    }
}
