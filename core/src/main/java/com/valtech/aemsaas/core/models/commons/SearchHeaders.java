package com.valtech.aemsaas.core.models.commons;

import org.apache.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class SearchHeaders {
    public Map<String, String> headers;

    public SearchHeaders() {
        headers = new HashMap<>();
    }

    public SearchHeaders addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public void createHeaders(HttpRequest request) {
        this.headers.forEach(request::addHeader);
    }
}
