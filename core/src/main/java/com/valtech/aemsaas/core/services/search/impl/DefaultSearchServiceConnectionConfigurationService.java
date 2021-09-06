package com.valtech.aemsaas.core.services.search.impl;

import com.valtech.aemsaas.core.services.search.SearchServiceConnectionConfigurationService;
import com.valtech.aemsaas.core.services.search.impl.DefaultSearchServiceConnectionConfigurationService.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Component(name = "Search as a Service - Search Service Connection Configuration Service",
    immediate = true,
    service = SearchServiceConnectionConfigurationService.class)
@Designate(ocd = Configuration.class)
@Slf4j
public class DefaultSearchServiceConnectionConfigurationService implements SearchServiceConnectionConfigurationService {

  private Configuration configuration;

  @Override
  public String getBaseUrl() {
    return configuration.searchService_baseurl();
  }

  @Override
  public String getBasicAuthenticationUser() {
    return configuration.searchService_basicAuthentication_user();
  }

  @Override
  public String getBasicAuthenticationPassword() {
    return configuration.searchService_basicAuthentication_password();
  }

  @Override
  public boolean isBasicAuthenticationEnabled() {
    return configuration.searchService_basicAuthentication_enable();
  }

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

  @Activate
  @Modified
  private void activate(Configuration configuration) {
    this.configuration = configuration;
  }

  @ObjectClassDefinition(name = "Search as a Service - Search Service HTTP Connection Configuration",
      description = "URL and credentials to connect to Search as a Service (SAAS).")
  public @interface Configuration {

    String DEFAULT_WEB_SERVICE_URL = "https://test-search-admin.infocentric.swiss";
    int DEFAULT_TIMEOUT = 10;
    boolean DEFAULT_BASIC_AUTHENTICATION_ENABLE = false;
    boolean DEFAULT_IGNORE_SSL = false;

    @AttributeDefinition(name = "Base URL",
        description = "The protocol + url for the search service")
    String searchService_baseurl() default DEFAULT_WEB_SERVICE_URL;

    @AttributeDefinition(name = "Basic authentication - User",
        description = "User for basic authentication to the search service")
    String searchService_basicAuthentication_user();

    @AttributeDefinition(name = "Basic authentication - Password",
        description = "Password for basic authentication to the search service",
        type = AttributeType.PASSWORD)
    String searchService_basicAuthentication_password();

    @AttributeDefinition(name = "Use basic authentication",
        description = "Submit the above user + password to the search service",
        type = AttributeType.BOOLEAN)
    boolean searchService_basicAuthentication_enable() default DEFAULT_BASIC_AUTHENTICATION_ENABLE;

    @AttributeDefinition(name = "Ignore invalid SSL certificate",
        description = "This setting should not be activated on a Production. Use for self-signed certificates if not in the trust store or for invalid certificates",
        type = AttributeType.BOOLEAN)
    boolean searchService_ignoreSSL() default DEFAULT_IGNORE_SSL;

    @AttributeDefinition(name = "HTTP Connection Timeout",
        description = "Timeout in seconds until a connection is established. A timeout value of zero is interpreted as an infinite timeout. Default is 10s ",
        type = AttributeType.INTEGER)
    int searchService_httpConnectionTimeout() default DEFAULT_TIMEOUT;

    @AttributeDefinition(name = "HTTP Connection Timeout",
        description = "Timeout in seconds for waiting for data or a maximum period of inactivity between two consecutive data packets. Default is 10s ",
        type = AttributeType.INTEGER)
    int searchService_httpSocketTimeout() default DEFAULT_TIMEOUT;

  }
}
