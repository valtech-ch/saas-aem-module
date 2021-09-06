package com.valtech.aemsaas.core.models.response.search;

import java.util.List;
import lombok.Value;

@Value
public class ResponseBody {
  public static final String PN_RESPONSE = "response";

  int numFound;

  int start;

  List<SearchResult> docs;
}
