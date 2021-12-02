package com.valtech.aem.saas.core.common.response;

import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JsonResponseCommitterTest {

    @Mock
    SlingHttpServletResponse response;

    @Mock
    PrintWriter printWriter;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testFlush() throws IOException {
        when(response.getWriter()).thenReturn(printWriter);
        new JsonResponseCommitter(response).flush(printWriter -> printWriter.write("foo"));
        verify(response, never()).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    void testFlush_errorOnWrite() throws IOException {
        when(response.getWriter()).thenThrow(IOException.class);
        new JsonResponseCommitter(response).flush(printWriter -> printWriter.write("foo"));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    void testNonNullArguments() {
        Assertions.assertThrows(NullPointerException.class, () -> new JsonResponseCommitter(null));
        Assertions.assertThrows(NullPointerException.class, () -> new JsonResponseCommitter(null, null));
    }
}
