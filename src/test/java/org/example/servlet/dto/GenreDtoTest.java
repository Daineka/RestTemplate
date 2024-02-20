package org.example.servlet.dto;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GenreDtoTest {

    @Test
    void testEqualsAndHashCode() {
        GenreDto genre1 = new GenreDto(1L, "Genre1");
        GenreDto genre2 = new GenreDto(1L, "Genre1");
        GenreDto genre3 = new GenreDto(2L, "Genre2");

        assertEquals(genre1, genre2);
        assertNotEquals(genre1, genre3);

        assertEquals(genre1.hashCode(), genre2.hashCode());
        assertNotEquals(genre1.hashCode(), genre3.hashCode());
    }

    @Test
    void testGettersAndSetters() {
        GenreDto genre = new GenreDto();
        genre.setId(1L);
        genre.setName("Genre1");

        assertEquals(1L, genre.getId());
        assertEquals("Genre1", genre.getName());
    }

    @Test
    void testSetBooks() {
        GenreDto genre = new GenreDto();
        Set<BookDto> books = new HashSet<>();
        books.add(new BookDto(1L, "Book1", 2000, null));
        books.add(new BookDto(2L, "Book2", 2020, null));

        genre.setBooks(books);

        assertEquals(2, genre.getBooks().size());
    }
}
