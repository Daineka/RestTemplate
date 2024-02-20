package org.example.servlet.mapper;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.example.servlet.dto.AuthorDto;
import org.example.servlet.dto.BookDto;
import org.example.servlet.dto.GenreDto;

import java.util.stream.Collectors;

public class GenreDtoMapperImpl implements GenreDtoMapper {

    public GenreDto toDto(Genre genre) {
        if (genre == null) {
            return null;
        }
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        genreDto.setBooks(genre.getBooks().stream()
                .map(this::mapBookToBookDto)
                .collect(Collectors.toSet()));
        return genreDto;
    }

    public Genre toEntity(GenreDto genreDto) {
        if (genreDto == null) {
            return null;
        }
        Genre genre = new Genre();
        genre.setId(genreDto.getId());
        genre.setName(genreDto.getName());
        genre.setBooks(genreDto.getBooks().stream()
                .map(this::mapBookDtoToBook)
                .collect(Collectors.toSet()));
        return genre;

    }

    private BookDto mapBookToBookDto(Book book) {
        if (book == null) {
            return null;
        }

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setPublishedYear(book.getPublishedYear());
        bookDto.setAuthor(mapAuthorToAuthorDto(book.getAuthor()));
        return bookDto;
    }

    private Book mapBookDtoToBook(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }

        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setPublishedYear(bookDto.getPublishedYear());
        book.setAuthor(mapAuthorDtoToAuthor(bookDto.getAuthor()));
        return book;
    }

    private AuthorDto mapAuthorToAuthorDto(Author author) {
        if (author == null) {
            return null;
        }

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        return authorDto;
    }

    private Author mapAuthorDtoToAuthor(AuthorDto authorDto) {
        if (authorDto == null) {
            return null;
        }

        Author author = new Author();
        author.setId(authorDto.getId());
        author.setName(authorDto.getName());
        return author;
    }
}
