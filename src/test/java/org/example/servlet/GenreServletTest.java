package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.GenreService;
import org.example.servlet.dto.GenreDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GenreServletTest {

    @Mock
    private GenreService genreServiceMock;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    private GenreServlet genreServlet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ObjectMapper objectMapper = new ObjectMapper();
        genreServlet = new GenreServlet(genreServiceMock, objectMapper);
    }

    @Test
    void testDoGet_withValidGenreId() throws Exception {
        long genreId = 1;
        when(requestMock.getParameter("genreId")).thenReturn(String.valueOf(genreId));

        GenreDto genreDto = new GenreDto();
        genreDto.setId(genreId);
        genreDto.setName("Test Genre");

        when(genreServiceMock.get(genreId)).thenReturn(genreDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(responseMock.getWriter()).thenReturn(writer);

        genreServlet.doGet(requestMock, responseMock);

        String responseContent = stringWriter.toString();
        String expectedContent = "{\"id\":1,\"name\":\"Test Genre\",\"books\":[]}";
        assertEquals(expectedContent, responseContent);

        verify(responseMock).setContentType("application/json");
        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
        verify(responseMock).getWriter();
        verify(genreServiceMock).get(genreId);
    }

    @Test
    void testDoGet_withInvalidGenreId() throws Exception {
        String invalidGenreId = "invalid";

        when(requestMock.getParameter("genreId")).thenReturn(invalidGenreId);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(responseMock.getWriter()).thenReturn(writer);

        genreServlet.doGet(requestMock, responseMock);

        String responseContent = stringWriter.toString();
        assertEquals("", responseContent);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoGet_withNullGenreId() throws Exception {
        when(requestMock.getParameter("genreId")).thenReturn(null);

        List<GenreDto> genres = new ArrayList<>();
        GenreDto genreDto1 = new GenreDto();
        genreDto1.setId(1L);
        genreDto1.setName("Genre 1");
        GenreDto genreDto2 = new GenreDto();
        genreDto2.setId(2L);
        genreDto2.setName("Genre 2");
        genres.add(genreDto1);
        genres.add(genreDto2);

        when(genreServiceMock.getAll()).thenReturn(genres);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(responseMock.getWriter()).thenReturn(writer);

        genreServlet.doGet(requestMock, responseMock);

        String responseContent = stringWriter.toString();
        String expectedContent = "[{\"id\":1,\"name\":\"Genre 1\",\"books\":[]},{\"id\":2,\"name\":\"Genre 2\",\"books\":[]}]";
        assertEquals(expectedContent, responseContent);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPost_withValidParameters() throws Exception {
        String name = "Test Genre";
        GenreDto genreDto = new GenreDto();
        genreDto.setId(1L);
        genreDto.setName(name);

        when(requestMock.getParameter("name")).thenReturn(name);
        when(genreServiceMock.save(any(GenreDto.class))).thenReturn(genreDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(responseMock.getWriter()).thenReturn(writer);

        genreServlet.doPost(requestMock, responseMock);

        String responseContent = stringWriter.toString();
        String expectedContent = "{\"id\":1,\"name\":\"Test Genre\",\"books\":[]}";
        assertEquals(expectedContent, responseContent);

        verify(responseMock).setContentType("application/json");
        verify(responseMock).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPost_withInvalidParameters() throws Exception {
        String invalidName = "";

        when(requestMock.getParameter("name")).thenReturn(invalidName);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(responseMock.getWriter()).thenReturn(writer);

        genreServlet.doPost(requestMock, responseMock);

        String responseContent = stringWriter.toString();
        assertEquals("", responseContent);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPut_withValidParameters() throws Exception {
        long genreId = 1L;
        String genreName = "New Genre";
        when(requestMock.getParameter("genreId")).thenReturn(String.valueOf(genreId));
        when(requestMock.getParameter("name")).thenReturn(genreName);

        GenreDto genreDto = new GenreDto();
        genreDto.setId(genreId);
        genreDto.setName(genreName);
        when(genreServiceMock.get(genreId)).thenReturn(genreDto);

        GenreDto updatedGenre = new GenreDto();
        updatedGenre.setId(genreId);
        updatedGenre.setName(genreName);
        when(genreServiceMock.update(any(GenreDto.class))).thenReturn(updatedGenre);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(responseMock.getWriter()).thenReturn(writer);

        genreServlet.doPut(requestMock, responseMock);

        String responseContent = stringWriter.toString();
        String expectedContent = "{\"id\":1,\"name\":\"New Genre\",\"books\":[]}";
        assertEquals(expectedContent, responseContent);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
        verify(responseMock).getWriter();
        verify(genreServiceMock).update(genreDto);
    }

    @Test
    void testDoPut_withInvalidGenreId() throws Exception {
        String invalidGenreId = "invalid";
        String name = "Updated Genre";

        when(requestMock.getParameter("genreId")).thenReturn(invalidGenreId);
        when(requestMock.getParameter("name")).thenReturn(name);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(responseMock.getWriter()).thenReturn(writer);

        genreServlet.doPut(requestMock, responseMock);

        String responseContent = stringWriter.toString();
        assertEquals("", responseContent);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoDelete_withValidGenreIdParameter() throws Exception {
        long genreId = 1;
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genreId);

        when(requestMock.getParameter("genreId")).thenReturn(String.valueOf(genreId));
        when(genreServiceMock.get(genreId)).thenReturn(genreDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(responseMock.getWriter()).thenReturn(writer);

        genreServlet.doDelete(requestMock, responseMock);

        String responseContent = stringWriter.toString();
        assertEquals("", responseContent);

        verify(responseMock).setContentType("application/json");
        verify(responseMock).setStatus(HttpServletResponse.SC_NO_CONTENT);
        verify(genreServiceMock).delete(genreDto);
    }

    @Test
    void testDoDelete_withInvalidGenreIdParameter() throws Exception {
        long invalidGenreId = -1;
        when(requestMock.getParameter("genreId")).thenReturn(String.valueOf(invalidGenreId));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(responseMock.getWriter()).thenReturn(writer);

        genreServlet.doDelete(requestMock, responseMock);

        String responseContent = stringWriter.toString();
        assertEquals("", responseContent);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
