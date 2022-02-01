package com.valtech.aem.saas.core.http.request;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a POST search request. It requires the request uri and the payload in form of {@link HttpEntity}.
 */
@Slf4j
@Builder
public class SearchRequestPost implements SearchRequest {

    @NonNull
    private final String uri;

    private final HttpEntity httpEntity;

    @Override
    public HttpUriRequest getRequest() {
        if (StringUtils.isBlank(uri)) {
            throw new IllegalArgumentException("Request uri must not be blank.");
        }
        HttpPost httpPost = new HttpPost(uri);
        if (httpEntity != null) {
            httpPost.setEntity(httpEntity);
        }
        return httpPost;
    }

    @Override
    public List<Integer> getSuccessStatusCodes() {
        return Arrays.asList(HttpServletResponse.SC_OK, HttpServletResponse.SC_CREATED);
    }
}
