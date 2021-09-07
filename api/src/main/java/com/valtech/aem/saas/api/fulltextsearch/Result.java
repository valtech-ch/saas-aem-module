package com.valtech.aem.saas.api.fulltextsearch;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Result {

  String url;
  String title;
  String description;
}
