package com.valtech.aem.saas.core.http.request;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a PUT search request. It requires the request uri and the payload in form of {@link HttpEntity}.
 */
@Slf4j
@Builder
public class SearchRequestPut implements SearchRequest {

    @NonNull
    private final String uri;

    private final HttpEntity httpEntity;

    @Override
    public HttpUriRequest getRequest() {
        if (StringUtils.isBlank(uri)) {
            throw new IllegalArgumentException("Request uri must not be blank.");
        }
        HttpPut httpPut = new HttpPut(uri);
        if (httpEntity != null) {
            httpPut.setEntity(httpEntity);
        }
        return httpPut;
    }

    @Override
    public List<Integer> getSuccessStatusCodes() {
        return Arrays.asList(HttpServletResponse.SC_OK,
                             HttpServletResponse.SC_CREATED,
                             HttpServletResponse.SC_NO_CONTENT);
    }
}
