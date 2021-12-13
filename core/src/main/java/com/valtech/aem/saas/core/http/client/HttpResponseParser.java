package com.valtech.aem.saas.core.http.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Helper class for parsing http response.
 */
@Slf4j
public final class HttpResponseParser {

    private final HttpResponse response;
    private final ByteArrayOutputStream responseContentCopy;

    public HttpResponseParser(@NonNull HttpResponse response) {
        this.response = response;
        this.responseContentCopy = copyToOutputStream();
    }

    /**
     * Gets the response content.
     *
     * @return response content string.
     */
    public String getContentString() {
        InputStream contentInputStream = getCopy();
        if (contentInputStream != null) {
            try {
                return IOUtils.toString(contentInputStream, StandardCharsets.UTF_8.name());
            } catch (IOException e) {
                log.error("IOException occurred while trying to parse the response {}", response);
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * Returns the response content serialized in a POJO.
     *
     * @param gsonModel the POJO type class.
     * @param <T>       the POJO type.
     * @return POJO of specified type.
     */
    public <T> T toGsonModel(Class<T> gsonModel) {
        InputStream in = getCopy();
        if (in != null) {
            try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8.name())) {
                return new Gson().fromJson(reader, gsonModel);
            } catch (IOException | IllegalStateException | JsonSyntaxException e) {
                log.error("Cannot serialize JSON", e);
            }
        }
        return null;
    }

    private InputStream getCopy() {
        if (responseContentCopy != null) {
            return new ByteArrayInputStream(responseContentCopy.toByteArray());
        }
        return null;
    }

    private ByteArrayOutputStream copyToOutputStream() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(response.getEntity().getContent(), baos);
            return baos;
        } catch (IOException e) {
            log.error("Error while fetching content input stream.", e);
        }
        return null;
    }
}
