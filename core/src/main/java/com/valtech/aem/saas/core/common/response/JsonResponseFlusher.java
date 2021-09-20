package com.valtech.aem.saas.core.common.response;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.servlets.post.JSONResponse;

@Slf4j
public class JsonResponseFlusher {

  private final SlingHttpServletResponse response;

  public JsonResponseFlusher(@NonNull final SlingHttpServletResponse response,
      @NonNull final String characterEncoding) {
    this.response = response;
    response.setContentType(JSONResponse.RESPONSE_CONTENT_TYPE);
    response.setCharacterEncoding(characterEncoding);
  }

  public JsonResponseFlusher(@NonNull final SlingHttpServletResponse response) {
    this(response, StandardCharsets.UTF_8.name());
  }

  public void flush(Consumer<PrintWriter> writerConsumer) {
    try {
      PrintWriter out = response.getWriter();
      writerConsumer.accept(out);
      out.flush();
    } catch (IOException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      log.error("Error occurred while writing in response.", e);
    }
  }

}
