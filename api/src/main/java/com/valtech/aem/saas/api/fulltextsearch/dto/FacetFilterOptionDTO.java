package com.valtech.aem.saas.api.fulltextsearch.dto;


import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class FacetFilterOptionDTO {

  String value;
  int hits;
}
