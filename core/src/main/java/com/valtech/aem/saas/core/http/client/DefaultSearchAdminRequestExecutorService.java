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

import static com.valtech.aem.saas.core.http.client.DefaultSearchAdminRequestExecutorService.CONFIGURATION_PID;

@Slf4j
@Component(name = "Search as a Service - Search Admin Request Executor Service",
           service = SearchAdminRequestExecutorService.class,
           immediate = true,
           configurationPid = CONFIGURATION_PID)
@Designate(ocd = DefaultSearchAdminRequestExecutorService.Configuration.class)
public class DefaultSearchAdminRequestExecutorService extends AbstractSearchRequestExecutorService implements SearchAdminRequestExecutorService {

    static final String CONFIGURATION_PID = "com.valtech.aem.saas.core.http.client.DefaultSearchAdminRequestExecutorService";

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
        return configuration.searchAdminRequestExecutorService_baseurl();
    }

    @Activate
    @Modified
    private void activate(Configuration configuration) {
        this.configuration = configuration;
        initHttpClient();
    }

    protected void prepareHttpClientFactoryBuilder(SearchHttpClientFactory.SearchHttpClientFactoryBuilder searchHttpClientFactoryBuilder) {
        searchHttpClientFactoryBuilder.jwtAuthenticationEnabled(configuration.searchAdminRequestExecutorService_jwtAuthentication_enable())
                                      .jwtToken(configuration.searchAdminRequestExecutorService_jwtAuthentication_token())
                                      .basicAuthenticationEnabled(configuration.searchAdminRequestExecutorService_basicAuthentication_enable())
                                      .baseUrl(configuration.searchAdminRequestExecutorService_baseurl())
                                      .basicAuthenticationUser(configuration.searchAdminRequestExecutorService_basicAuthentication_user())
                                      .basicAuthenticationUser(configuration.searchAdminRequestExecutorService_basicAuthentication_password());
    }

    @Deactivate
    private void deactivate() {
        closeClient();
    }

    @ObjectClassDefinition(name = "Search as a Service - Search Admin Request Executor Service Configuration",
                           description = "URL and authentication details for connect to Search Admin Endpoint.")
    public @interface Configuration {

        String DEFAULT_WEB_SERVICE_URL = "https://ic-test-search-admin.valtech.swiss/admin";
        boolean DEFAULT_BASIC_AUTHENTICATION_ENABLE = false;
        boolean DEFAULT_JWT_AUTHENTICATION_ENABLE = false;

        @AttributeDefinition(name = "Base URL",
                             description = "The protocol + url for the search service") String searchAdminRequestExecutorService_baseurl() default DEFAULT_WEB_SERVICE_URL; // NOSONAR

        @AttributeDefinition(name = "Basic authentication - User",
                             description = "User for basic authentication to the search service") String searchAdminRequestExecutorService_basicAuthentication_user(); // NOSONAR

        @AttributeDefinition(name = "Basic authentication - Password",
                             description = "Password for basic authentication to the search service",
                             type = AttributeType.PASSWORD) String searchAdminRequestExecutorService_basicAuthentication_password(); // NOSONAR

        @AttributeDefinition(name = "Use basic authentication",
                             description = "Submit the above user + password to the search service",
                             type = AttributeType.BOOLEAN) boolean searchAdminRequestExecutorService_basicAuthentication_enable() default DEFAULT_BASIC_AUTHENTICATION_ENABLE; // NOSONAR

        @AttributeDefinition(name = "JWT authentication token",
                             description = "Token string",
                             type = AttributeType.PASSWORD) String searchAdminRequestExecutorService_jwtAuthentication_token(); // NOSONAR

        @AttributeDefinition(name = "Use JWT authentication",
                             description = "Set above authorization token in search service request.",
                             type = AttributeType.BOOLEAN) boolean searchAdminRequestExecutorService_jwtAuthentication_enable() default DEFAULT_JWT_AUTHENTICATION_ENABLE; // NOSONAR

    }
}
