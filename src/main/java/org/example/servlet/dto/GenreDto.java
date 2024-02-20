package org.example.servlet.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GenreDto {
    private Long id;
    private String name;

    private Set<BookDto> books = new HashSet<>();

    public GenreDto() {
    }

    public GenreDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public GenreDto(Long id, String name, Set<BookDto> books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }

    public Set<BookDto> getBooks() {
        return books;
    }

    public void setBooks(Set<BookDto> books) {
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreDto genreDto = (GenreDto) o;
        return Objects.equals(id, genreDto.id) && Objects.equals(name, genreDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
