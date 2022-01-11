package com.valtech.aem.saas.core.http.request;

import com.valtech.aem.saas.api.request.SearchRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public final class SearchRequestHead implements SearchRequest {
    @NonNull
    private final String uri;

    @Override
    public HttpUriRequest getRequest() {
        if (StringUtils.isBlank(uri)) {
            throw new IllegalArgumentException("Request uri must not be blank.");
        }
        return new HttpHead(uri);
    }

    @Override
    public List<Integer> getSuccessStatusCodes() {
        return Collections.singletonList(HttpServletResponse.SC_OK);
    }
}
