package com.valtech.aem.saas.core.http.response.dto;

import java.util.List;
import lombok.Value;

@Value
public class ResponseBodyDto {

  public static final String PN_RESPONSE = "response";

  int numFound;

  int start;

  List<SearchResultDto> docs;
}
