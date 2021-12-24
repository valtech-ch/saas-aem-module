package com.valtech.aem.saas.it.tests;

import com.adobe.cq.testing.junit.rules.CQAuthorClassRule;
import com.valtech.aem.saas.api.fulltextsearch.SearchTabModel;
import com.valtech.aem.saas.api.query.LanguageQuery;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.testing.clients.ClientException;
import org.apache.sling.testing.clients.util.ResourceUtil;
import org.codehaus.jackson.JsonNode;
import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class SearchComponentIT {

  public static final String EXPECTED_SEARCH_MODEL_JSON = "search.model.json";
  @ClassRule
  public static final CQAuthorClassRule cqBaseClassRule = new CQAuthorClassRule();
  @ClassRule
  public static final SearchContentInstallRule content = new SearchContentInstallRule(cqBaseClassRule.authorRule);

  public static final String RESULTS = "results";
  public static final String RESULTS_TOTAL = "resultsTotal";
  public static final String ITEMS = "items";
  public static final String FACET_FILTERS = "facetFilters";
  public static final String TEXT = "text";
  public static final String SUGGESTION = "suggestion";
  public static final String JSON_EXPORTER_MODEL = "model";
  public static final String JSON_EXTENSION = "json";



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
    List<NameValuePair> searchParams = Arrays.asList(new BasicNameValuePair(SearchTabModel.SEARCH_TERM, "uk"),
                                                     new BasicNameValuePair(LanguageQuery.KEY, "en"));

    JsonNode searchTab1 = slingModelJsonExporterClient.doGetJsonNode(
            getJsonModelPreparedUrl(
                    "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab"),
            searchParams,
            200);

    JsonNode searchTab2 = slingModelJsonExporterClient.doGetJsonNode(
            getJsonModelPreparedUrl(
                    "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab_765476375"),
            searchParams, 200);

    JsonNode searchTab3CopyOfSearchTab1WithTmplSet = slingModelJsonExporterClient.doGetJsonNode(
            getJsonModelPreparedUrl(
                    "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab_copy"),
            searchParams, 200);

    validateSearchTabJson(searchTab1);
    validateSearchTabJson(searchTab2);
    validateSearchTabJson(searchTab3CopyOfSearchTab1WithTmplSet);

    searchResultsExist(searchTab1);
    searchResultsExist(searchTab2);
    searchResultsExist(searchTab3CopyOfSearchTab1WithTmplSet);

    hasFacetFilters(searchTab1);
    hasFacetFilters(searchTab3CopyOfSearchTab1WithTmplSet);

    assertNotEquals(getResultsTotal(searchTab1), getResultsTotal(searchTab2));

    //check that setting a tmpl param retrieves different results
    assertNotEquals(getResultsTotal(searchTab1), getResultsTotal(searchTab3CopyOfSearchTab1WithTmplSet));
  }

  @Test
  public void testAutocomplete() throws ClientException {
    JsonNode jsonNode = slingModelJsonExporterClient.doGetJsonNode(
        "/content/saas-aem-module/us/en/jcr:content/root/container/search.autocomplete.json",
        Collections.singletonList(new BasicNameValuePair(SearchTabModel.SEARCH_TERM, "foo")),
        200);
    assertNotNull(jsonNode);
    assertTrue(jsonNode.isArray());
    assertTrue(jsonNode.size() > 0);
  }

  @Test
  public void testAutosuggest() throws ClientException {
    List<NameValuePair> searchParams = Arrays.asList(new BasicNameValuePair(SearchTabModel.SEARCH_TERM, "fooo"),
        new BasicNameValuePair(LanguageQuery.KEY, "en"));

    JsonNode searchTab1 = slingModelJsonExporterClient.doGetJsonNode(
        getJsonModelPreparedUrl(
            "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab"), searchParams,
        200);

    validateSearchTabJson(searchTab1);
    assertEquals("food", getSuggestionText(searchTab1));

  }

  private String getSuggestionText(JsonNode searchTab) {
    JsonNode suggestion = searchTab.get(SUGGESTION);
    assertTrue(suggestion.isObject());
    JsonNode text = suggestion.get(TEXT);
    assertNotNull(text);
    assertTrue(text.isTextual());
    return text.getTextValue();
  }

  private void hasFacetFilters(JsonNode searchTab) {
    JsonNode facetFilters = searchTab.get(FACET_FILTERS);
    assertNotNull(facetFilters);
    assertTrue(facetFilters.isObject());
    JsonNode items = facetFilters.get(ITEMS);
    assertNotNull(items);
    assertTrue(items.isArray());
    assertTrue(items.size() > 0);
  }

  private int getResultsTotal(JsonNode searchTab) {
    JsonNode resultsTotal = searchTab.get(RESULTS_TOTAL);
    assertNotNull(resultsTotal);
    return resultsTotal.getIntValue();
  }

  private void validateSearchTabJson(JsonNode searchTab) {
    assertNotNull(searchTab);
    assertTrue(searchTab.isObject());
  }

  private void searchResultsExist(JsonNode searchTab) {
    JsonNode results = searchTab.get(RESULTS);
    assertNotNull(results);
    assertTrue(results.isArray());
    assertTrue(results.size() > 0);
  }

  private String getJsonModelPreparedUrl(String path) {
    return String.format("%s.%s.%s", path, JSON_EXPORTER_MODEL, JSON_EXTENSION);
  }
}
