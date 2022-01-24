package com.valtech.aem.saas.core.http.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import static com.valtech.aem.saas.core.http.client.DefaultSearchApiRequestExecutorService.CONFIGURATION_PID;


@Slf4j
@Component(name = "Search as a Service - Search Api Request Executor Service",
           service = SearchApiRequestExecutorService.class,
           immediate = true,
           configurationPid = CONFIGURATION_PID)
@Designate(ocd = DefaultSearchApiRequestExecutorService.Configuration.class)
public class DefaultSearchApiRequestExecutorService extends AbstractSearchRequestExecutorService implements SearchApiRequestExecutorService {

    static final String CONFIGURATION_PID = "com.valtech.aem.saas.core.http.client.DefaultSearchApiRequestExecutorService";

    @Getter(AccessLevel.PROTECTED)
    @Reference
    private HttpClientBuilderFactory httpClientBuilderFactory;

    private Configuration configuration;

    @Reference
    @Synchronized
    protected void bindSearchConnectionConfig(SearchServiceConnectionConfigurationService service) {
        setSearchConnectionConfig(service);
    }

    @Synchronized
    protected void updatedSearchConnectionConfig(SearchServiceConnectionConfigurationService service) {
        setSearchConnectionConfig(service);
        initHttpClient();
    }

    @Override
    public String getBaseUrl() {
        return configuration.searchApiRequestExecutorService_baseurl();
    }

    @Activate
    @Modified
    private void activate(Configuration configuration) {
        this.configuration = configuration;
        initHttpClient();
    }

    @Override
    protected void prepareHttpClientFactoryBuilder(SearchHttpClientFactory.SearchHttpClientFactoryBuilder searchHttpClientFactoryBuilder) {
        searchHttpClientFactoryBuilder.jwtAuthenticationEnabled(configuration.searchApiRequestExecutorService_jwtAuthentication_enable())
                                      .jwtToken(configuration.searchApiRequestExecutorService_jwtAuthentication_token());
    }

    @Deactivate
    private void deactivate() {
        closeClient();
    }

    @ObjectClassDefinition(name = "Search as a Service - Search Api Request Executor Service Configuration",
                           description = "URL and authentication details for connect to Search Api Endpoint.")
    public @interface Configuration {

        String DEFAULT_WEB_SERVICE_URL = "https://ic-test-search-api.valtech.swiss";
        boolean DEFAULT_JWT_AUTHENTICATION_ENABLE = false;

        @AttributeDefinition(name = "Base URL",
                             description = "The protocol + url for the search service")
        String searchApiRequestExecutorService_baseurl() default DEFAULT_WEB_SERVICE_URL; // NOSONAR

        @AttributeDefinition(name = "JWT authentication token",
                             description = "Token string",
                             type = AttributeType.PASSWORD)
        String searchApiRequestExecutorService_jwtAuthentication_token(); // NOSONAR

        @AttributeDefinition(name = "Use JWT authentication",
                             description = "Set above authorization token in search service request.",
                             type = AttributeType.BOOLEAN)
        boolean searchApiRequestExecutorService_jwtAuthentication_enable() default DEFAULT_JWT_AUTHENTICATION_ENABLE; // NOSONAR

    }
}
