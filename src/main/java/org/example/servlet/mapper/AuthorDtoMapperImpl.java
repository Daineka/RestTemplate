package org.example.servlet.mapper;

import org.example.model.Author;
import org.example.model.Book;
import org.example.servlet.dto.AuthorDto;
import org.example.servlet.dto.BookDto;

import java.util.stream.Collectors;

public class AuthorDtoMapperImpl implements AuthorDtoMapper {

    @Override
    public AuthorDto toDto(Author author) {
        if (author == null) {
            return null;
        }

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        authorDto.setBooks(author.getBooks().stream()
                .map(this::mapBookToBookDto)
                .collect(Collectors.toSet()));
        return authorDto;
    }

    @Override
    public Author toEntity(AuthorDto authorDto) {
        if (authorDto == null) {
            return null;
        }

        Author author = new Author();
        author.setId(authorDto.getId());
        author.setName(authorDto.getName());
        author.setBooks(authorDto.getBooks().stream()
                .map(this::mapBookDtoToBook)
                .collect(Collectors.toSet()));
        return author;
    }

    private BookDto mapBookToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setPublishedYear(book.getPublishedYear());
        return bookDto;
    }

    private Book mapBookDtoToBook(BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setPublishedYear(bookDto.getPublishedYear());
        return book;
    }
}
