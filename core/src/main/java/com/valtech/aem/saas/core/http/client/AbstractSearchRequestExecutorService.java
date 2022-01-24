package com.valtech.aem.saas.core.http.client;

import com.valtech.aem.saas.api.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.osgi.services.HttpClientBuilderFactory;

import java.util.Optional;

@Slf4j
public abstract class AbstractSearchRequestExecutorService implements SearchRequestExecutorService {

    @Getter(value = AccessLevel.PROTECTED,
            onMethod_ = {@Synchronized})
    @Setter(value = AccessLevel.PROTECTED,
            onMethod_ = {@Synchronized})
    private SearchServiceConnectionConfigurationService searchConnectionConfig;

    private CloseableHttpClient httpClient;

    @Override
    public Optional<SearchResponse> execute(@NonNull SearchRequest searchRequest) {
        return new SearchRequestExecutor(httpClient).execute(searchRequest);
    }

    protected abstract void prepareHttpClientFactoryBuilder(SearchHttpClientFactory.SearchHttpClientFactoryBuilder searchHttpClientFactoryBuilder);

    protected abstract HttpClientBuilderFactory getHttpClientBuilderFactory();

    protected void initHttpClient() {
        SearchHttpClientFactory.SearchHttpClientFactoryBuilder builder = SearchHttpClientFactory.builder();
        builder.httpClientBuilderFactory(getHttpClientBuilderFactory())
               .httpConnectionTimeout(getSearchConnectionConfig().getHttpConnectionTimeout())
               .httpSocketTimeout(getSearchConnectionConfig().getHttpSocketTimeout())
               .ignoreSslEnabled(getSearchConnectionConfig().isIgnoreSslEnabled())
               .httpMaxTotalConnections(getSearchConnectionConfig().getHttpMaxTotalConnections())
               .httpMaxConnectionsPerRoute(getSearchConnectionConfig().getHttpMaxConnectionsPerRoute());
        prepareHttpClientFactoryBuilder(builder);
        httpClient = builder.build().create();
    }

    protected void closeClient() {
        IOUtils.closeQuietly(httpClient, e -> log.error("Could not close client.", e));
    }

}
