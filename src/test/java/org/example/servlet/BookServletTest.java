package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.BookService;
import org.example.servlet.dto.BookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookServletTest {

    private BookServlet servlet;

    @Mock
    private BookService bookService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new BookServlet(bookService, objectMapper);
    }

    @Test
    void testDoGet_withBookIdParam() throws Exception {
        long bookId = 1L;
        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);
        bookDto.setTitle("Sample Book");

        when(request.getParameter("bookId")).thenReturn(String.valueOf(bookId));
        when(bookService.get(bookId)).thenReturn(bookDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);

        String responseContent = stringWriter.toString();
        String expectedContent = objectMapper.writeValueAsString(bookDto);
        assertEquals(expectedContent, responseContent);
    }

    @Test
    void testDoGet_withoutBookIdParam() throws Exception {
        List<BookDto> books = new ArrayList<>();
        BookDto bookDto1 = new BookDto();
        bookDto1.setId(1L);
        bookDto1.setTitle("Sample Book 1");
        books.add(bookDto1);

        BookDto bookDto2 = new BookDto();
        bookDto2.setId(2L);
        bookDto2.setTitle("Sample Book 2");
        books.add(bookDto2);

        when(bookService.getAll()).thenReturn(books);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);

        String responseContent = stringWriter.toString();
        String expectedContent = objectMapper.writeValueAsString(books);
        assertEquals(expectedContent, responseContent);
    }

    @Test
    void testDoPost_withValidParameters() throws Exception {
        String title = "Sample Book";
        int publishedYear = 2022;
        long authorId = 1L;

        BookDto createdBook = new BookDto();
        createdBook.setId(1L);
        createdBook.setTitle(title);

        when(request.getParameter("title")).thenReturn(title);
        when(request.getParameter("publishedYear")).thenReturn(String.valueOf(publishedYear));
        when(request.getParameter("authorId")).thenReturn(String.valueOf(authorId));
        when(bookService.save(any(BookDto.class))).thenReturn(createdBook);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_CREATED);

        String responseContent = stringWriter.toString();
        String expectedContent = objectMapper.writeValueAsString(createdBook);
        assertEquals(expectedContent, responseContent);
    }

    @Test
    void testDoPost_withInvalidParameters() throws Exception {
        when(request.getParameter("title")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPut_withValidParameters() throws Exception {
        long bookId = 1L;
        String title = "Updated Book";
        int publishedYear = 2023;
        long authorId = 2L;

        BookDto existingBook = new BookDto();
        existingBook.setId(bookId);
        existingBook.setTitle("Sample Book");

        BookDto updatedBook = new BookDto();
        updatedBook.setId(bookId);
        updatedBook.setTitle(title);

        when(request.getParameter("bookId")).thenReturn(String.valueOf(bookId));
        when(request.getParameter("title")).thenReturn(title);
        when(request.getParameter("publishedYear")).thenReturn(String.valueOf(publishedYear));
        when(request.getParameter("authorId")).thenReturn(String.valueOf(authorId));

        when(bookService.get(bookId)).thenReturn(existingBook);
        when(bookService.update(existingBook)).thenReturn(updatedBook);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPut(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);

        String responseContent = stringWriter.toString();
        String expectedContent = objectMapper.writeValueAsString(updatedBook);
        assertEquals(expectedContent, responseContent);
    }

    @Test
    void testDoPut_withInvalidParameters() throws Exception {
        when(request.getParameter("bookId")).thenReturn(null);

        servlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoDelete_withValidBookId() throws Exception {
        long bookId = 1L;
        BookDto existingBook = new BookDto();
        existingBook.setId(bookId);

        when(request.getParameter("bookId")).thenReturn(String.valueOf(bookId));
        when(bookService.get(bookId)).thenReturn(existingBook);

        servlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        verify(bookService).delete(existingBook);
    }

    @Test
    void testDoDelete_withInvalidBookId() throws Exception {
        when(request.getParameter("bookId")).thenReturn(null);

        servlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
