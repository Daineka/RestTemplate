package org.example.servlet.dto;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AuthorDtoTest {

    @Test
    void testEqualsAndHashCode() {
        AuthorDto author1 = new AuthorDto(1L, "Author1");
        author1.setBooks(Set.of(new BookDto(1L, "Book1", 2000, author1)));
        AuthorDto author2 = new AuthorDto(1L, "Author1");
        author2.setBooks(Set.of(new BookDto(1L, "Book1", 2000, author2)));
        AuthorDto author3 = new AuthorDto(2L, "Author2");
        author3.setBooks(Set.of(new BookDto(2L, "Book2", 2020, author3)));

        assertEquals(author1, author2);
        assertNotEquals(author1, author3);

        assertEquals(author1.hashCode(), author2.hashCode());
        assertNotEquals(author1.hashCode(), author3.hashCode());
    }

    @Test
    void testGettersAndSetters() {
        AuthorDto author = new AuthorDto();
        author.setId(1L);
        author.setName("Author1");
        Set<BookDto> books = new HashSet<>();
        books.add(new BookDto(1L, "Book1", 2000, author));
        books.add(new BookDto(2L, "Book2", 2020, author));
        author.setBooks(books);

        assertEquals(1L, author.getId());
        assertEquals("Author1", author.getName());
        assertEquals(books, author.getBooks());
    }
}
