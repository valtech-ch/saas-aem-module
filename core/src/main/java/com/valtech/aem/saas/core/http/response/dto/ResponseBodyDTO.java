package com.valtech.aem.saas.core.http.response.dto;

import java.util.List;
import lombok.Value;

@Value
public class ResponseBodyDTO {

  public static final String PN_RESPONSE = "response";

  int numFound;

  int start;

  List<SearchResultDTO> docs;
}
