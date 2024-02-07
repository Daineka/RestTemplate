package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.AuthorService;
import org.example.service.impl.AuthorServiceImpl;
import org.example.servlet.dto.AuthorDto;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/authors")
public class AuthorServlet extends HttpServlet {

    private final transient AuthorService authorService;
    private final ObjectMapper objectMapper;

    public AuthorServlet() {
        this.authorService = new AuthorServiceImpl();
        this.objectMapper = new ObjectMapper();
    }

    public AuthorServlet(AuthorService authorService, ObjectMapper objectMapper) {
        this.authorService = authorService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String authorIdParam = req.getParameter("authorId");
        if (authorIdParam == null) {
            displayAuthorsList(resp);
        } else {
            if (isNotValidAuthorId(authorIdParam)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                long authorId = Long.parseLong(authorIdParam);
                displaySingleAuthor(resp, authorId);
            }
        }
    }

    void displayAuthorsList(HttpServletResponse resp) throws IOException {
        List<AuthorDto> authors = authorService.getAll();
        try (PrintWriter printWriter = resp.getWriter()) {
            objectMapper.writeValue(printWriter, authors);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    void displaySingleAuthor(HttpServletResponse resp, long authorId) throws IOException {
        AuthorDto authorDto = authorService.get(authorId);

        if (authorDto != null) {
            try (PrintWriter printWriter = resp.getWriter()) {
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(printWriter, authorDto);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        String name = req.getParameter("name");

        if (isNotValidParameter(name)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            AuthorDto authorDto = new AuthorDto();
            authorDto.setName(name);

            AuthorDto createdAuthor = authorService.save(authorDto);

            try (PrintWriter writer = resp.getWriter()) {
                objectMapper.writeValue(writer, createdAuthor);
            }

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");

        String authorIdParam = req.getParameter("authorId");
        String name = req.getParameter("name");

        if (isNotValidAuthorId(authorIdParam) || isNotValidParameter(name)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            long authorId = Long.parseLong(authorIdParam);

            AuthorDto existingAuthor = authorService.get(authorId);
            if (existingAuthor == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            existingAuthor.setName(name);

            AuthorDto updatedAuthor = authorService.update(existingAuthor);

            try (PrintWriter writer = resp.getWriter()) {
                objectMapper.writeValue(writer, updatedAuthor);
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

        String authorIdParam = req.getParameter("authorId");

        if (isNotValidAuthorId(authorIdParam)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            long authorId = Long.parseLong(authorIdParam);

            AuthorDto existingAuthor = authorService.get(authorId);
            if (existingAuthor == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            authorService.delete(existingAuthor);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    private boolean isNotValidParameter(String param) {
        return param == null || param.isEmpty();
    }

    private boolean isNotValidAuthorId(String authorIdParam) {
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
