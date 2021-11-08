package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class MockFilterModel implements FilterModel {

  String name;
  String value;
}
