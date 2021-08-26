package com.valtech.aemsaas.core.services;

public interface SearchServiceConnectionConfigurationService {

  String getBaseUrl();

  String getBasicAuthenticationUser();

  String getBasicAuthenticationPassword();

  boolean isBasicAuthenticationEnabled();

  boolean isIgnoreSslEnabled();

  int getHttpConnectionTimeout();

  int getHttpSocketTimeout();
}
