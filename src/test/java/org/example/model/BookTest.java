package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BookTest {

    @Test
    void testGetterAndSetter() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setPublishedYear(2022);

        assertEquals(1L, book.getId());
        assertEquals("Test Book", book.getTitle());
        assertEquals(2022, book.getPublishedYear());
    }

    @Test
    void testEqualsAndHashCode() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Test Book");
        book1.setPublishedYear(2022);

        Book book2 = new Book();
        book2.setId(1L);
        book2.setTitle("Test Book");
        book2.setPublishedYear(2022);

        assertEquals(book1, book2);
        assertEquals(book1.hashCode(), book2.hashCode());
    }
}
