package org.example.servlet.mapper;

import org.example.model.Book;
import org.example.servlet.dto.BookDto;

public interface BookDtoMapper extends DtoMapper<Book, BookDto> {


    @Override
    BookDto toDto(Book book);

    @Override
    Book toEntity(BookDto bookDto);
}
