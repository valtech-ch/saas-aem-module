package com.valtech.aemsaas.core.models.search;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Singular;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

@Builder
public class FacetsQuery implements FulltextSearchGetQuery {

  static final String FACETFIELD = "facetfield";

  @Singular
  private final List<String> fields;

  @Override
  public List<NameValuePair> getEntries() {
    return fields.stream().filter(StringUtils::isNotBlank).map(field -> new BasicNameValuePair(FACETFIELD, field))
        .collect(Collectors.toList());
  }

}
