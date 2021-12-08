package com.valtech.aem.saas.core.http.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public final class HttpResponseParser {

    private final HttpResponse response;

    /**
     * Gets the response content.
     *
     * @return response content string.
     */
    public String getContentString() {
        InputStream contentInputStream = getContentInputStream();
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
        InputStream in = getContentInputStream();
        if (in != null) {
            try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8.name())) {
                return new Gson().fromJson(reader, gsonModel);
            } catch (IOException | IllegalStateException | JsonSyntaxException e) {
                log.error("Cannot serialize JSON", e);
            }
        }
        return null;
    }

    private InputStream getContentInputStream() {
        try {
            return getCopy(response.getEntity().getContent());
        } catch (IOException e) {
            log.error("Error while fetching content input stream.", e);
        }
        return null;
    }

    private InputStream getCopy(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }
}
