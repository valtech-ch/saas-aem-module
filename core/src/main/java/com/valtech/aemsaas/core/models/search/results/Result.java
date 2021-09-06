package com.valtech.aemsaas.core.models.search.results;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Result {

  String url;
  String title;
  String description;
}
