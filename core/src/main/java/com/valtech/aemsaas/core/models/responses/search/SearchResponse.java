package com.valtech.aemsaas.core.models.responses.search;

import java.util.List;
import java.util.Map;
import lombok.Value;

@Value
public class SearchResponse {

  ResponseHeader responseHeader;

  ResponseBody response;

  FacetCounts facetCounts;

  Map<String, Map<String, List<String>>> highlighting;

}
