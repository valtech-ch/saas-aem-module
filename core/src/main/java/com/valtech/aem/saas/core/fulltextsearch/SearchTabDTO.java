package com.valtech.aem.saas.core.fulltextsearch;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SearchTabDTO {

  String url;
  String name;
}
