package com.valtech.aem.saas.core.common.response;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.servlets.post.JSONResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Helper class that sets content via the response's writer and commits the response.
 */
@Slf4j
public final class JsonResponseCommitter {

    private final SlingHttpServletResponse response;

    /**
     * Instantiates an object that will commit the passed response object with the specified character encoding value.
     *
     * @param response          the response object to be committed.
     * @param characterEncoding the char encoding to be set on the response object.
     */
    public JsonResponseCommitter(
            @NonNull final SlingHttpServletResponse response,
            @NonNull final String characterEncoding) {
        this.response = response;
        response.setContentType(JSONResponse.RESPONSE_CONTENT_TYPE);
        response.setCharacterEncoding(characterEncoding);
    }

    /**
     * Instantiates an object that sets UTF-8 char encoding on the response object.
     *
     * @param response the response object to be committed.
     */
    public JsonResponseCommitter(@NonNull final SlingHttpServletResponse response) {
        this(response, StandardCharsets.UTF_8.name());
    }

    /**
     * Flushes the writer right after it applies the writer consuming logic passed as method parameter.
     *
     * @param writerConsumer lambda function containing writer consuming logic.
     */
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
