package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorTest {

    @Test
    void testGetterAndSetter() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Test Author");

        assertEquals(1L, author.getId());
        assertEquals("Test Author", author.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Test Author");

        Author author2 = new Author();
        author2.setId(1L);
        author2.setName("Test Author");

        assertEquals(author1, author2);
        assertEquals(author1.hashCode(), author2.hashCode());
    }
}
