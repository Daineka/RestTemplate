package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.AuthorService;
import org.example.servlet.dto.AuthorDto;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class AuthorServletTest {

    private AuthorServlet servlet;

    @Mock
    private AuthorService authorService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new AuthorServlet(authorService, objectMapper);
    }


    @Test
    void testDoGet_withAuthorIdParam() throws Exception {
        long authorId = 1L;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName("John Doe");

        when(request.getParameter("authorId")).thenReturn(String.valueOf(authorId));
        when(authorService.get(authorId)).thenReturn(authorDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);

        String responseContent = stringWriter.toString();
        String expectedContent = objectMapper.writeValueAsString(authorDto);
        assertEquals(expectedContent, responseContent);
    }

    @Test
    void testDoGet_withoutAuthorIdParam() throws Exception {
        List<AuthorDto> authors = new ArrayList<>();
        AuthorDto authorDto1 = new AuthorDto();
        authorDto1.setId(1L);
        authorDto1.setName("John Doe");
        authors.add(authorDto1);

        AuthorDto authorDto2 = new AuthorDto();
        authorDto2.setId(2L);
        authorDto2.setName("Jane Smith");
        authors.add(authorDto2);

        when(authorService.getAll()).thenReturn(authors);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);

        String responseContent = stringWriter.toString();
        String expectedContent = objectMapper.writeValueAsString(authors);
        assertEquals(expectedContent, responseContent);
    }

    @Test
    void testDoPost_withValidParameters() throws Exception {
        String name = "John Doe";
        AuthorDto createdAuthor = new AuthorDto();
        createdAuthor.setId(1L);
        createdAuthor.setName(name);

        when(request.getParameter("name")).thenReturn(name);
        when(authorService.save(any(AuthorDto.class))).thenReturn(createdAuthor);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_CREATED);

        String responseContent = stringWriter.toString();
        String expectedContent = objectMapper.writeValueAsString(createdAuthor);
        assertEquals(expectedContent, responseContent);
    }

    @Test
    void testDoPost_withInvalidParameters() throws Exception {
        String invalidName = "";
        when(request.getParameter("name")).thenReturn(invalidName);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void testDoPut_withValidParameters() throws Exception {
        long authorId = 1L;
        String name = "John Doe";

        AuthorDto existingAuthor = new AuthorDto();
        existingAuthor.setId(authorId);
        existingAuthor.setName("Old Name");

        AuthorDto updatedAuthor = new AuthorDto();
        updatedAuthor.setId(authorId);
        updatedAuthor.setName(name);

        when(request.getParameter("authorId")).thenReturn(String.valueOf(authorId));
        when(request.getParameter("name")).thenReturn(name);
        when(authorService.get(authorId)).thenReturn(existingAuthor);
        when(authorService.update(existingAuthor)).thenReturn(updatedAuthor);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPut(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);

        String responseContent = stringWriter.toString();
        String expectedContent = objectMapper.writeValueAsString(updatedAuthor);
        assertEquals(expectedContent, responseContent);
    }

    @Test
    void testDoPut_withInvalidParameters() throws Exception {
        String invalidAuthorId = "";
        String invalidName = "";

        when(request.getParameter("authorId")).thenReturn(invalidAuthorId);
        when(request.getParameter("name")).thenReturn(invalidName);

        servlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void testDoDelete_withValidAuthorId() throws Exception {
        long authorId = 1L;
        AuthorDto existingAuthor = new AuthorDto();
        existingAuthor.setId(authorId);

        when(request.getParameter("authorId")).thenReturn(String.valueOf(authorId));
        when(authorService.get(authorId)).thenReturn(existingAuthor);

        servlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        verify(authorService).delete(existingAuthor);
    }

    @Test
    void testDoDelete_withInvalidAuthorId() throws Exception {
        String invalidAuthorId = "";
        when(request.getParameter("authorId")).thenReturn(invalidAuthorId);

        servlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verifyNoMoreInteractions(authorService);
    }


}
