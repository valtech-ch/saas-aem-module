package com.valtech.aem.saas.core.http.client;

import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Component(immediate = true,
           service = SearchServiceConnectionConfigurationService.class)
@ServiceDescription("Search as a Service - Search Service Connection Configuration Service")
@Designate(ocd = Configuration.class)
@Slf4j
public class DefaultSearchServiceConnectionConfigurationService implements SearchServiceConnectionConfigurationService {

    private Configuration configuration;

    @Override
    public boolean isIgnoreSslEnabled() {
        return configuration.searchService_ignoreSSL();
    }

    @Override
    public int getHttpConnectionTimeout() {
        return configuration.searchService_httpConnectionTimeout();
    }

    @Override
    public int getHttpSocketTimeout() {
        return configuration.searchService_httpSocketTimeout();
    }

    @Override
    public int getHttpMaxTotalConnections() {
        return configuration.searchService_httpMaxTotalConnections();
    }

    @Override
    public int getHttpMaxConnectionsPerRoute() {
        return configuration.searchService_httpMaxConnectionsPerRoute();
    }

    @Activate
    @Modified
    private void activate(Configuration configuration) {
        this.configuration = configuration;
    }

    @ObjectClassDefinition(name = "Search as a Service - Search Service HTTP Connection Configuration",
            description = "Connect details for SaaS APIs.")
    public @interface Configuration {

        int DEFAULT_TIMEOUT = 10000;
        boolean DEFAULT_IGNORE_SSL = false;
        int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;
        int DEFAULT_MAX_TOTAL_CONNECTIONS_PER_ROUTE = 50;

        @AttributeDefinition(name = "Ignore invalid SSL certificate",
                description = "This setting should not be activated on a Production. Use for self-signed certificates if not in the trust store or for invalid certificates",
                type = AttributeType.BOOLEAN)
        boolean searchService_ignoreSSL() default DEFAULT_IGNORE_SSL; // NOSONAR

        @AttributeDefinition(name = "HTTP Connection Timeout",
                description = "Timeout in milliseconds until a connection is established. A timeout value of zero is interpreted as an infinite timeout. Default is 10000ms ",
                type = AttributeType.INTEGER)
        int searchService_httpConnectionTimeout() default DEFAULT_TIMEOUT; // NOSONAR

        @AttributeDefinition(name = "HTTP Connection Timeout",
                description = "Timeout in milliseconds for waiting for data or a maximum period of inactivity between two consecutive data packets. Default is 10000ms ",
                type = AttributeType.INTEGER)
        int searchService_httpSocketTimeout() default DEFAULT_TIMEOUT; // NOSONAR

        @AttributeDefinition(name = "HTTP Client Max Total Connections",
                description = "How many connections an http client can have",
                type = AttributeType.INTEGER)
        int searchService_httpMaxTotalConnections() default DEFAULT_MAX_TOTAL_CONNECTIONS; // NOSONAR

        @AttributeDefinition(name = "HTTP Client Max Connections Per Route",
                description = "How many connections an http client can have per route",
                type = AttributeType.INTEGER)
        int searchService_httpMaxConnectionsPerRoute() default DEFAULT_MAX_TOTAL_CONNECTIONS_PER_ROUTE; // NOSONAR

    }
}
