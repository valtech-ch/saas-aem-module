package com.valtech.aem.saas.core.http.response;

import java.util.List;
import lombok.Value;

@Value
public class ResponseBody {
  public static final String PN_RESPONSE = "response";

  int numFound;

  int start;

  List<SearchResult> docs;
}
