package com.valtech.aem.saas.it.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.adobe.cq.testing.junit.rules.CQAuthorClassRule;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

    JsonNode jsonNode = slingModelJsonExporterClient.doGetJsonNode(
        getJsonModelPreparedUrl("/content/saas-aem-module/us/en/jcr:content/root/container/search"), 200);
    String expectedJson = IOUtils.toString(ResourceUtil.getResourceAsStream(EXPECTED_SEARCH_MODEL_JSON),
        StandardCharsets.UTF_8.name());
    JSONAssert.assertEquals(expectedJson, jsonNode.toString(), false);
  }

  @Test
  public void testSearchResults() throws ClientException {
    List<NameValuePair> searchParams = Arrays.asList(new BasicNameValuePair("q", "abb"),
        new BasicNameValuePair("lang", "en"));

    JsonNode searchTab1 = slingModelJsonExporterClient.doGetJsonNode(
        getJsonModelPreparedUrl(
            "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab"), searchParams,
        200);

    JsonNode searchTab2 = slingModelJsonExporterClient.doGetJsonNode(
        getJsonModelPreparedUrl(
            "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab_765476375"),
        searchParams, 200);

    validateSearchTabJson(searchTab1);
    validateSearchTabJson(searchTab2);

    searchResultsExist(searchTab1);
    searchResultsExist(searchTab2);

    hasFacetFilters(searchTab1);

    assertNotEquals(getResultsTotal(searchTab1), getResultsTotal(searchTab2));
  }

  @Test
  public void testAutocomplete() throws ClientException {
    JsonNode jsonNode = slingModelJsonExporterClient.doGetJsonNode(
        "/content/saas-aem-module/us/en/jcr:content/root/container/search.autocomplete.json",
        Collections.singletonList(new BasicNameValuePair("q", "foo")),
        200);
    assertNotNull(jsonNode);
    assertTrue(jsonNode.isArray());
    assertTrue(jsonNode.size() > 0);
  }

  @Test
  public void testAutosuggest() throws ClientException {
    List<NameValuePair> searchParams = Arrays.asList(new BasicNameValuePair("q", "fooo"),
        new BasicNameValuePair("lang", "en"));

    JsonNode searchTab1 = slingModelJsonExporterClient.doGetJsonNode(
        getJsonModelPreparedUrl(
            "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab"), searchParams,
        200);

    validateSearchTabJson(searchTab1);
    assertEquals("food", getSuggestionText(searchTab1));

  }

  private String getSuggestionText(JsonNode searchTab) {
    JsonNode suggestion = searchTab.get("suggestion");
    assertTrue(suggestion.isObject());
    JsonNode text = suggestion.get("text");
    assertNotNull(text);
    assertTrue(text.isTextual());
    return text.getTextValue();
  }

  private void hasFacetFilters(JsonNode searchTab) {
    JsonNode facetFilters = searchTab.get("facetFilters");
    assertNotNull(facetFilters);
    assertTrue(facetFilters.isObject());
    JsonNode items = facetFilters.get("items");
    assertNotNull(items);
    assertTrue(items.isArray());
    assertTrue(items.size() > 0);
  }

  private int getResultsTotal(JsonNode searchTab) {
    JsonNode resultsTotal = searchTab.get("resultsTotal");
    assertNotNull(resultsTotal);
    return resultsTotal.getIntValue();
  }

  private void validateSearchTabJson(JsonNode searchTab) {
    assertNotNull(searchTab);
    assertTrue(searchTab.isObject());
  }

  private void searchResultsExist(JsonNode searchTab) {
    JsonNode results = searchTab.get("results");
    assertNotNull(results);
    assertTrue(results.isArray());
    assertTrue(results.size() > 0);
  }

  private String getJsonModelPreparedUrl(String path) {
    return path + ".model.json";
  }
}
