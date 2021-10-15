package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.valtech.aem.saas.api.fulltextsearch.Suggestion;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * A strategy for extracting highlighting data.
 */
@Slf4j
public final class SuggestionDataExtractionStrategy implements SearchResponseDataExtractionStrategy<Suggestion> {

  public static final String PN_SPELLCHECK = "spellcheck";
  public static final String PN_COLLATION_QUERY = "collationQuery";
  public static final String PN_COLLATIONS = "collations";
  public static final String PN_HITS = "hits";

  @Override
  public String propertyName() {
    return PN_SPELLCHECK;
  }

  @Override
  public Optional<Suggestion> getData(JsonElement response) {
    return Optional.ofNullable(response)
        .filter(JsonElement::isJsonObject)
        .map(JsonElement::getAsJsonObject)
        .map(r -> r.getAsJsonObject(propertyName()))
        .map(this::getCollations)
        .map(this::getCollation)
        .filter(this::isCollationQueryExisting)
        .map(jsonObject -> new Suggestion(jsonObject.getAsJsonPrimitive(PN_COLLATION_QUERY).getAsString(),
            jsonObject.getAsJsonPrimitive(
                PN_HITS).getAsInt()));
  }

  private JsonArray getCollations(JsonObject spellcheck) {
    try {
      if (spellcheck.has(PN_COLLATIONS)) {
        return spellcheck.get(PN_COLLATIONS).getAsJsonArray();
      }
    } catch (IllegalStateException e) {
      log.error("Error while fetching collations");
    }
    return null;
  }

  private JsonObject getCollation(JsonArray collations) {
    try {
      if (collations.size() > 1) {
        return collations.get(1).getAsJsonObject();
      }
    } catch (IllegalStateException e) {
      log.error("Error while fetching collation query");
    }
    return null;
  }

  private boolean isCollationQueryExisting(JsonObject collation) {
    JsonElement collationQuery = collation.get(PN_COLLATION_QUERY);
    return collationQuery != null && collationQuery.isJsonPrimitive() && collationQuery.getAsJsonPrimitive().isString();
  }
}
