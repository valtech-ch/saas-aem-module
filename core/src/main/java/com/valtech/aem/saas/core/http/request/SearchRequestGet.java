package com.valtech.aem.saas.core.http.request;

import com.valtech.aem.saas.api.request.SearchRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * Represents a GET search request. It requires the request uri, including the query string.
 */
@RequiredArgsConstructor
public class SearchRequestGet implements SearchRequest {

    @NonNull
    private final String uri;

    @Override
    public HttpUriRequest getRequest() {
        if (StringUtils.isBlank(uri)) {
            throw new IllegalArgumentException("Request uri must not be blank.");
        }
        return new HttpGet(uri);
    }

    @Override
    public List<Integer> getSuccessStatusCodes() {
        return Collections.singletonList(HttpServletResponse.SC_OK);
    }
}
