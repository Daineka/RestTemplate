package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.GenreService;
import org.example.service.impl.GenreServiceImpl;
import org.example.servlet.dto.GenreDto;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/genres")
public class GenreServlet extends HttpServlet {

    private final transient GenreService genreService;
    private final ObjectMapper objectMapper;

    public GenreServlet(GenreService genreService, ObjectMapper objectMapper) {
        this.genreService = genreService;
        this.objectMapper = objectMapper;
    }

    public GenreServlet() {
        this.genreService = new GenreServiceImpl();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String genreIdParam = req.getParameter("genreId");
        if (genreIdParam == null) {
            displayGenresList(resp);
        } else {
            if (isNotValidGenreId(genreIdParam)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                long genreId = Long.parseLong(genreIdParam);
                displaySingleGenre(resp, genreId);
            }
        }
    }

    private void displayGenresList(HttpServletResponse resp) {
        List<GenreDto> genres = genreService.getAll();
        try (PrintWriter printWriter = resp.getWriter()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(printWriter, genres);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    private void displaySingleGenre(HttpServletResponse resp, long genreId) throws IOException {
        GenreDto genreDto = genreService.get(genreId);
        if (genreDto != null) {
            try (PrintWriter printWriter = resp.getWriter()) {
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(printWriter, genreDto);
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
            GenreDto genreDto = new GenreDto();
            genreDto.setName(name);

            GenreDto createdGenre = genreService.save(genreDto);

            try (PrintWriter writer = resp.getWriter()) {
                objectMapper.writeValue(writer, createdGenre);
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

        String genreIdParam = req.getParameter("genreId");
        String name = req.getParameter("name");

        if (isNotValidGenreId(genreIdParam) || isNotValidParameter(name)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            long genreId = Long.parseLong(genreIdParam);

            GenreDto existingGenre = genreService.get(genreId);
            if (existingGenre == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            existingGenre.setName(name);

            GenreDto updatedGenre = genreService.update(existingGenre);

            try (PrintWriter writer = resp.getWriter()) {
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(writer, updatedGenre);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");

        String genreIdParam = req.getParameter("genreId");

        if (isNotValidGenreId(genreIdParam)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            long genreId = Long.parseLong(genreIdParam);

            GenreDto existingGenre = genreService.get(genreId);
            if (existingGenre == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            genreService.delete(existingGenre);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    private boolean isNotValidParameter(String param) {
        return param == null || param.isEmpty();
    }

    private boolean isNotValidGenreId(String genreIdParam) {
        if (isNotValidParameter(genreIdParam)) {
            return true;
        }
        try {
            long genreId = Long.parseLong(genreIdParam);
            return genreId < 1;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
