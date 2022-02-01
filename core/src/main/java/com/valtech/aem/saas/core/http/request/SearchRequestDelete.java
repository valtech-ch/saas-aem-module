package com.valtech.aem.saas.core.http.request;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * Represents a DELETE search request with request body. It requires the request uri and the payload in form of {@link
 * HttpEntity}.
 */
@Slf4j
@Builder
public class SearchRequestDelete implements SearchRequest {

    @NonNull
    private final String uri;
    private final HttpEntity httpEntity;

    @Override
    public HttpUriRequest getRequest() {
        if (StringUtils.isBlank(uri)) {
            throw new IllegalArgumentException("Request uri must not be blank.");
        }
        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(uri);
        if (httpEntity != null) {
            httpDelete.setEntity(httpEntity);
        }
        return httpDelete;
    }

    @Override
    public List<Integer> getSuccessStatusCodes() {
        return Collections.singletonList(HttpServletResponse.SC_OK);
    }
}
