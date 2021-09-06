package com.valtech.aemsaas.core.models.sling.search;

import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

public interface Filter {

  @ValueMapValue
  String getName();

  @ValueMapValue
  String getValue();
}
