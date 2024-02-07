package org.example.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenreTest {

    @Test
    void testGetterAndSetter() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Test Genre");

        assertEquals(1L, genre.getId());
        assertEquals("Test Genre", genre.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        Genre genre1 = new Genre();
        genre1.setId(1L);
        genre1.setName("Test Genre");

        Genre genre2 = new Genre();
        genre2.setId(1L);
        genre2.setName("Test Genre");

        assertEquals(genre1, genre2);
        assertEquals(genre1.hashCode(), genre2.hashCode());
    }

    @Test
    void testSetAndGetBooks() {
        Genre genre = new Genre();
        Book book1 = new Book();
        Book book2 = new Book();

        Set<Book> books = new HashSet<>();
        books.add(book1);
        books.add(book2);

        genre.setBooks(books);

        assertEquals(books, genre.getBooks());
    }
}
