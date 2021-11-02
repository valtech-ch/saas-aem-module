package com.valtech.aem.saas.api.query;

import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class MockFilterModel implements FilterModel {

  String name;
  String value;
}
