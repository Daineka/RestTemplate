package org.example.servlet.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BookDto {
    private Long id;
    private String title;
    private Integer publishedYear;

    private AuthorDto author;

    private Set<GenreDto> genres = new HashSet<>();

    public BookDto() {
    }

    public BookDto(Long id, String title, Integer publishedYear, AuthorDto author, Set<GenreDto> genres) {
        this.id = id;
        this.title = title;
        this.publishedYear = publishedYear;
        this.author = author;
        this.genres = genres;
    }

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }

    public BookDto(Long id, String title, Integer publishedYear, AuthorDto author) {
        this.id = id;
        this.title = title;
        this.publishedYear = publishedYear;
        this.author = author;
    }

    public Set<GenreDto> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreDto> genres) {
        this.genres = genres;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDto bookDto = (BookDto) o;
        return Objects.equals(id, bookDto.id) && Objects.equals(title, bookDto.title) && Objects.equals(publishedYear, bookDto.publishedYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, publishedYear);
    }
}
