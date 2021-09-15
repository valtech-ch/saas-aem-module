package com.valtech.aem.saas.core.typeahead;

import com.valtech.aem.saas.api.typeahead.TypeaheadConsumerService;
import com.valtech.aem.saas.api.typeahead.TypeaheadPayload;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.request.SearchRequestGet;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import com.valtech.aem.saas.core.http.response.TypeaheadDataExtractionStrategy;
import com.valtech.aem.saas.core.query.DefaultLanguageQuery;
import com.valtech.aem.saas.core.query.FiltersQuery;
import com.valtech.aem.saas.core.query.GetQueryStringConstructor;
import com.valtech.aem.saas.core.query.TypeaheadTextQuery;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

@Builder
public final class IndexTypeaheadConsumerService implements TypeaheadConsumerService {

  private final SearchRequestExecutorService searchRequestExecutorService;
  private final String apiUrl;
  @Singular
  private final List<String> allowedFilterFields;

  @Override
  public List<String> getResults(@NonNull TypeaheadPayload typeaheadPayload) {
    validateCommonConfigs();
    if (StringUtils.isBlank(typeaheadPayload.getText())) {
      throw new IllegalArgumentException("Typeahead payload should contain a search text.");
    }
    if (StringUtils.isBlank(typeaheadPayload.getLanguage())) {
      throw new IllegalArgumentException("Typeahead payload should contain a language.");
    }
    validateFilterFields(typeaheadPayload);

    SearchRequestGet searchRequestGet = new SearchRequestGet(apiUrl + getQueryString(typeaheadPayload));
    return searchRequestExecutorService.execute(searchRequestGet)
        .filter(SearchResponse::isSuccess)
        .flatMap(response -> response.get(new TypeaheadDataExtractionStrategy(typeaheadPayload.getLanguage())))
        .orElse(Collections.emptyList());
  }

  private String getQueryString(@NonNull TypeaheadPayload typeaheadPayload) {
    GetQueryStringConstructor.GetQueryStringConstructorBuilder builder =
        GetQueryStringConstructor.builder()
            .query(new TypeaheadTextQuery(typeaheadPayload.getText()))
            .query(new DefaultLanguageQuery(typeaheadPayload.getLanguage()));
    if (MapUtils.isNotEmpty(typeaheadPayload.getFilterEntries())) {
      FiltersQuery.FiltersQueryBuilder filtersQueryBuilder = FiltersQuery.builder();
      typeaheadPayload.getFilterEntries().forEach(filtersQueryBuilder::filterEntry);
      builder.query(filtersQueryBuilder.build());
    }
    return builder.build().getQueryString();
  }

  private void validateCommonConfigs() {
    if (searchRequestExecutorService == null) {
      throw new IllegalStateException("Missing an instance of SearchRequestExecutorService.");
    }
    if (StringUtils.isBlank(apiUrl)) {
      throw new IllegalStateException("Typeahead api base url must be specified.");
    }
  }

  private void validateFilterFields(@NonNull TypeaheadPayload typeaheadPayload) {
    if (CollectionUtils.isNotEmpty(allowedFilterFields)) {
      List<String> forbiddenFilterFields = typeaheadPayload.getFilterEntries().keySet().stream()
          .filter(field -> !allowedFilterFields.contains(field)).collect(
              Collectors.toList());
      if (CollectionUtils.isNotEmpty(forbiddenFilterFields)) {
        throw new IllegalArgumentException(
            String.format("The following filter field names are not allowed: %s", forbiddenFilterFields));
      }
    }
  }
}
