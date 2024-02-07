package org.example.servlet.dto;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BookDtoTest {

    @Test
    void testEqualsAndHashCode() {
        AuthorDto author1 = new AuthorDto(1L, "Author1");
        AuthorDto author2 = new AuthorDto(2L, "Author2");
        BookDto book1 = new BookDto(1L, "Book1", 2000, author1);
        BookDto book2 = new BookDto(1L, "Book1", 2000, author1);
        BookDto book3 = new BookDto(2L, "Book2", 2020, author2);

        assertEquals(book1, book2);
        assertNotEquals(book1, book3);

        assertEquals(book1.hashCode(), book2.hashCode());
        assertNotEquals(book1.hashCode(), book3.hashCode());
    }

    @Test
    void testGettersAndSetters() {
        AuthorDto author = new AuthorDto(1L, "Author1");
        Set<GenreDto> genres = new HashSet<>();
        genres.add(new GenreDto(1L, "Genre1"));
        genres.add(new GenreDto(2L, "Genre2"));
        BookDto book = new BookDto();
        book.setId(1L);
        book.setTitle("Book1");
        book.setPublishedYear(2000);
        book.setAuthor(author);
        book.setGenres(genres);

        assertEquals(1L, book.getId());
        assertEquals("Book1", book.getTitle());
        assertEquals(2000, book.getPublishedYear());
        assertEquals(author, book.getAuthor());
        assertEquals(genres, book.getGenres());
    }
}
