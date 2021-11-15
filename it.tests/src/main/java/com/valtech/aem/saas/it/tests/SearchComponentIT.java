package com.valtech.aem.saas.it.tests;

import com.adobe.cq.testing.junit.rules.CQAuthorClassRule;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.apache.sling.testing.clients.ClientException;
import org.apache.sling.testing.clients.util.ResourceUtil;
import org.codehaus.jackson.JsonNode;
import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class SearchComponentIT {

  public static final String EXPECTED_SEARCH_MODEL_JSON = "search.model.json";
  @ClassRule
  public static final CQAuthorClassRule cqBaseClassRule = new CQAuthorClassRule();

  @Rule
  public SearchContentInstallRule content = new SearchContentInstallRule(cqBaseClassRule.authorRule);

  static SlingModelJsonExporterClient slingModelJsonExporterClient;

  @BeforeClass
  public static void beforeClass() {
    slingModelJsonExporterClient = cqBaseClassRule.authorRule.getAdminClient(SlingModelJsonExporterClient.class);
  }

  @Test
  public void testSearchModelJson() throws ClientException, JSONException, IOException {

    JsonNode jsonNode = slingModelJsonExporterClient.doGetModelJson(
        "/content/saas-aem-module/us/en/jcr:content/root/container/search", 200);
    String expectedJson = IOUtils.toString(ResourceUtil.getResourceAsStream(EXPECTED_SEARCH_MODEL_JSON),
        StandardCharsets.UTF_8.name());
    JSONAssert.assertEquals(expectedJson, jsonNode.toString(), false);
  }

}
