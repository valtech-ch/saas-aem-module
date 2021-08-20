package com.valtech.aemsaas.core.services.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import static com.valtech.aemsaas.core.util.Constants.*;


@ObjectClassDefinition(name = "SAAS 01 BaseSearchService", description = "URL and credentials to connect to Search as a Service (SAAS).")
public @interface BaseHttpServiceConfiguration {
    @AttributeDefinition(name = "Webservice URL", description = "The protocol + url for the search service")
    String search_webservice_baseurl() default DEFAULT_WEB_SERVICE_URL_KEY;

    @AttributeDefinition(name = "Query term in app", description = "Override default query param in app (default 'q')")
    String search_webservice_queryParam() default DEFAULT_WEB_SERVICE_QUERY_PARAM;

    @AttributeDefinition(name = "Webservice Auth User", description = "User for basic auth to the search service")
    String search_webservice_auth_user() default DEFAULT_WEB_SERVICE_USER;

    @AttributeDefinition(name = "Webservice Auth Password", description = "Password for basic auth to the search service")
    String search_webservice_auth_password() default DEFAULT_WEB_SERVICE_PASSWORD;

    @AttributeDefinition(name = "Auth enabled", description = "Submit the above user + password to the search service", type = AttributeType.BOOLEAN)
    boolean search_webservice_auth() default false;

    @AttributeDefinition(name = "Ignore invalid SSL certificate", description = "Use for self-signed certificates if not in the trust store or for invalid certificates", type = AttributeType.BOOLEAN)
    boolean search_webservice_ignoreSSL() default false;

    @AttributeDefinition(name = "HTTP Connection Timeout", description = "Timeout in seconds until a connection is established. A timeout value of zero is interpreted as an infinite timeout. Default is 10s ", type = AttributeType.INTEGER)
    int search_http_connection_timeout() default 10;

    @AttributeDefinition(name = "HTTP Connection Timeout", description = "Timeout in seconds for waiting for data or a maximum period of inactivity between two consecutive data packets. Default is 10s ", type = AttributeType.INTEGER)
    int search_http_socket_timeout() default 10;

}

