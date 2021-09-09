package com.valtech.aem.saas.api.fulltextsearch;

import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

public interface Filter {

  @ValueMapValue
  String getName();

  @ValueMapValue
  String getValue();
}
