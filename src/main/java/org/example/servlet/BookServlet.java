package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.BookService;
import org.example.service.impl.BookServiceImpl;
import org.example.servlet.dto.AuthorDto;
import org.example.servlet.dto.BookDto;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/books")
public class BookServlet extends HttpServlet {

    private final transient BookService bookService;
    private final ObjectMapper objectMapper;

    public BookServlet(BookService bookService, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.objectMapper = objectMapper;
    }

    public BookServlet() {
        this.bookService = new BookServiceImpl();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String bookIdParam = req.getParameter("bookId");
        if (bookIdParam == null) {
            displayBooksList(resp);
        } else {
            if (isNotValidId(bookIdParam)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                long bookId = Long.parseLong(bookIdParam);
                displaySingleBook(bookId, resp);
            }
        }
    }

    private void displayBooksList(HttpServletResponse resp) {
        List<BookDto> books = bookService.getAll();
        try (PrintWriter printWriter = resp.getWriter()) {
            objectMapper.writeValue(printWriter, books);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    private void displaySingleBook(long bookId, HttpServletResponse resp) throws IOException {
        BookDto bookDto = bookService.get(bookId);
        if (bookDto != null) {
            try (PrintWriter printWriter = resp.getWriter()) {
                objectMapper.writeValue(printWriter, bookDto);
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        try {
            String title = req.getParameter("title");
            String publishedYearParam = req.getParameter("publishedYear");
            String authorIdParam = req.getParameter("authorId");

            if (isNotValidId(authorIdParam) || isNotValidParameter(title) || isNotValidId(publishedYearParam)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            Integer publishedYear = Integer.parseInt(publishedYearParam);
            Long authorId = Long.parseLong(authorIdParam);

            BookDto bookDto = createBookDto(title, publishedYear, authorId);

            if (bookDto == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            BookDto createdBook = bookService.save(bookDto);

            try (PrintWriter writer = resp.getWriter()) {
                objectMapper.writeValue(writer, createdBook);
            }

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");

        String bookIdParam = req.getParameter("bookId");
        String title = req.getParameter("title");
        String publishedYearParam = req.getParameter("publishedYear");


        if (isNotValidId(bookIdParam) || isNotValidParameter(title) || isNotValidId(publishedYearParam)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Integer publishedYear = Integer.parseInt(publishedYearParam);

        try {
            long bookId = Long.parseLong(bookIdParam);

            BookDto existingBook = bookService.get(bookId);
            if (existingBook == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            existingBook.setTitle(title);
            existingBook.setPublishedYear(publishedYear);

            BookDto updatedBook = bookService.update(existingBook);

            try (PrintWriter writer = resp.getWriter()) {
                objectMapper.writeValue(writer, updatedBook);
            }

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");

        String bookIdParam = req.getParameter("bookId");

        if (isNotValidId(bookIdParam)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            long bookId = Long.parseLong(bookIdParam);

            BookDto existingBook = bookService.get(bookId);
            if (existingBook == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            bookService.delete(existingBook);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    private BookDto createBookDto(String title, Integer publishedYear, Long authorId) {
        if (title == null || publishedYear == null || authorId == null) {
            return null;
        }

        BookDto bookDto = new BookDto();
        bookDto.setTitle(title);
        bookDto.setPublishedYear(publishedYear);

        AuthorDto author = new AuthorDto();
        author.setId(authorId);
        bookDto.setAuthor(author);

        return bookDto;
    }

    private boolean isNotValidParameter(String param) {
        return param == null || param.isEmpty();
    }

    private boolean isNotValidId(String authorIdParam) {
        if (isNotValidParameter(authorIdParam)) {
            return true;
        }
        try {
            long authorId = Long.parseLong(authorIdParam);
            return authorId < 1;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
