package com.valtech.aem.saas.core.common.response;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsonResponseFlusherTest {

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
    new JsonResponseFlusher(response).flush(printWriter -> printWriter.write("foo"));
    verify(response, never()).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }

  @Test
  void testFlush_errorOnWrite() throws IOException {
    when(response.getWriter()).thenThrow(IOException.class);
    new JsonResponseFlusher(response).flush(printWriter -> printWriter.write("foo"));
    verify(response, times(1)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }

  @Test
  void testNonNullArguments() {
    Assertions.assertThrows(NullPointerException.class, () -> new JsonResponseFlusher(null));
    Assertions.assertThrows(NullPointerException.class, () -> new JsonResponseFlusher(null, null));
  }
}
