package com.valtech.aem.saas.api.query;

import lombok.Builder;
import lombok.Singular;
import org.apache.http.client.utils.URIBuilder;

import java.util.List;

/**
 * Helper class that constructs a query string from a list of {@link Query} objects. It uses {@link URIBuilder} to take
 * advantage of the query params escaping feature.
 */
@Builder
public class GetQueryStringConstructor {

    @Singular
    private List<Query> queries;


    /**
     * Returns the query string.
     *
     * @return query string.
     */
    public String getQueryString() {
        URIBuilder uriBuilder = new URIBuilder();
        queries.stream()
               .map(Query::getEntries)
               .forEach(uriBuilder::addParameters);
        return uriBuilder.toString();
    }
}
