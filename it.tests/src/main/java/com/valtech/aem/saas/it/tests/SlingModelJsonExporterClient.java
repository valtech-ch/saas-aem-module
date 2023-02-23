package com.valtech.aem.saas.it.tests;

import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.sling.testing.clients.ClientException;
import org.apache.sling.testing.clients.SlingClient;
import org.apache.sling.testing.clients.SlingClientConfig;
import org.apache.sling.testing.clients.SlingHttpResponse;
import org.apache.sling.testing.clients.util.HttpUtils;
import org.apache.sling.testing.clients.util.JsonUtils;

public final class SlingModelJsonExporterClient extends SlingClient {

  public SlingModelJsonExporterClient(CloseableHttpClient http,
      SlingClientConfig config) throws ClientException {
    super(http, config);
  }

  public SlingModelJsonExporterClient(URI url, String user, String password) throws ClientException {
    super(url, user, password);
  }

  public JsonNode doGetJsonNode(String path, int... expectedStatus) throws ClientException {

    SlingHttpResponse response = this.doGet(path, HttpUtils.getExpectedStatus(200, expectedStatus));
    return JsonUtils.getJsonNodeFromString(response.getContent());
  }

  public JsonNode doGetJsonNode(String path, List<NameValuePair> parameters, int... expectedStatus)
      throws ClientException {

    SlingHttpResponse response = this.doGet(path, parameters, HttpUtils.getExpectedStatus(200, expectedStatus));
    return JsonUtils.getJsonNodeFromString(response.getContent());
  }
}
