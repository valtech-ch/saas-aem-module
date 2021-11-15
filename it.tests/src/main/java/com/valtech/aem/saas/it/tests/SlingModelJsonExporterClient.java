package com.valtech.aem.saas.it.tests;

import java.net.URI;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.sling.testing.clients.ClientException;
import org.apache.sling.testing.clients.SlingClient;
import org.apache.sling.testing.clients.SlingClientConfig;
import org.apache.sling.testing.clients.SlingHttpResponse;
import org.apache.sling.testing.clients.util.HttpUtils;
import org.apache.sling.testing.clients.util.JsonUtils;
import org.codehaus.jackson.JsonNode;

public final class SlingModelJsonExporterClient extends SlingClient {

  public SlingModelJsonExporterClient(CloseableHttpClient http,
      SlingClientConfig config) throws ClientException {
    super(http, config);
  }

  public SlingModelJsonExporterClient(URI url, String user, String password) throws ClientException {
    super(url, user, password);
  }

  public JsonNode doGetModelJson(String path, int... expectedStatus) throws ClientException {
    path = path + ".model.json";

    SlingHttpResponse response = this.doGet(path, HttpUtils.getExpectedStatus(200, expectedStatus));
    return JsonUtils.getJsonNodeFromString(response.getContent());
  }
}
