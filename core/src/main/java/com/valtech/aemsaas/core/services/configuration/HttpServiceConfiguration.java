package com.valtech.aemsaas.core.services.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@ObjectClassDefinition(name = "Search as a Service - HTTP Service Configuration", description = "URL and credentials to connect to Search as a Service (SAAS).")
public @interface HttpServiceConfiguration {

    String DEFAULT_WEB_SERVICE_URL_KEY = "https://test-search.infocentric.swiss"; //this value needs to be updated
    int DEFAULT_TIMEOUT = 10;
    boolean FALSE = false;

    @AttributeDefinition(name = "Webservice URL",
            description = "The protocol + url for the search service")
    String search_webservice_baseurl() default DEFAULT_WEB_SERVICE_URL_KEY;

    @AttributeDefinition(name = "Webservice Basic authentication User",
            description = "User for basic authentication to the search service")
    String search_webservice_auth_user();

    @AttributeDefinition(name = "Webservice Basic authentication Password",
            description = "Password for basic authentication to the search service")
    String search_webservice_auth_password();

    @AttributeDefinition(name = "Basic authentication enabled",
            description = "Submit the above user + password to the search service",
            type = AttributeType.BOOLEAN)
    boolean search_webservice_auth() default FALSE;

    @AttributeDefinition(name = "Ignore invalid SSL certificate",
            description = "This setting should not be activated on a Production. Use for self-signed certificates if not in the trust store or for invalid certificates",
            type = AttributeType.BOOLEAN)
    boolean search_webservice_ignoreSSL() default FALSE;

    @AttributeDefinition(name = "HTTP Connection Timeout",
            description = "Timeout in seconds until a connection is established. A timeout value of zero is interpreted as an infinite timeout. Default is 10s ",
            type = AttributeType.INTEGER)
    int search_http_connection_timeout() default DEFAULT_TIMEOUT;

    @AttributeDefinition(name = "HTTP Connection Timeout",
            description = "Timeout in seconds for waiting for data or a maximum period of inactivity between two consecutive data packets. Default is 10s ",
            type = AttributeType.INTEGER)
    int search_http_socket_timeout() default DEFAULT_TIMEOUT;

}

