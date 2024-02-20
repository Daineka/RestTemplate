package org.example.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "book_id")
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "published_year")
    private Integer publishedYear;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToMany
    @JoinTable(name = "bookgenres",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    public Book() {
    }

    public Book(Long id, String title, Integer publishedYear, Author author) {
        this.id = id;
        this.title = title;
        this.publishedYear = publishedYear;
        this.author = author;
    }

    public Book(Long id, String title, Integer publishedYear, Author author, Set<Genre> genres) {
        this.id = id;
        this.title = title;
        this.publishedYear = publishedYear;
        this.author = author;
        this.genres = genres;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
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
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(publishedYear, book.publishedYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, publishedYear);
    }
}
