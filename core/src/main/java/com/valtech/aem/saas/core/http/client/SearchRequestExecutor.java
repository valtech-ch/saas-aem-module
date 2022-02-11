package com.valtech.aem.saas.core.http.client;

import com.google.gson.JsonElement;
import com.valtech.aem.saas.core.http.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public final class SearchRequestExecutor {
    private final CloseableHttpClient httpClient;

    public Optional<SearchResponse> execute(@NonNull SearchRequest searchRequest) {
        HttpUriRequest request = searchRequest.getRequest();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            log.info("Executing {} request on search api {}", request.getMethod(), request.getURI());
            log.info("Success status codes: {}", searchRequest.getSuccessStatusCodes());
            log.info("Status Code: {}", response.getStatusLine().getStatusCode());
            log.debug("Reason: {}", response.getStatusLine().getReasonPhrase());
            boolean isSuccess = isRequestSuccessful(searchRequest, response);
            HttpResponseParser httpResponseParser = new HttpResponseParser(response);
            if (log.isDebugEnabled()) {
                log.debug("Response content: {}", httpResponseParser.getContentString());
            }
            JsonElement jsonResponse = httpResponseParser.toGsonModel(JsonElement.class);
            return Optional.of(new SearchResponse(jsonResponse, isSuccess));
        } catch (IOException e) {
            log.error("Error while executing request", e);
        } finally {
            if (response != null) {
                IOUtils.closeQuietly(response, e -> log.error("Could not close response.", e));
            }
        }
        return Optional.empty();
    }

    private boolean isRequestSuccessful(
            SearchRequest searchRequest,
            HttpResponse httpResponse) {
        boolean success = searchRequest.getSuccessStatusCodes().contains(httpResponse.getStatusLine().getStatusCode());
        if (success) {
            log.debug("Request is successful.");
        } else {
            log.debug("Request has failed.");
        }
        return success;
    }
}
