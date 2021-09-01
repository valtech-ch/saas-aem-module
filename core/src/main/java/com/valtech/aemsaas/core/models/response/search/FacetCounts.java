package com.valtech.aemsaas.core.models.response.search;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.List;
import lombok.Value;

@Value
public class FacetCounts {

  @SerializedName("facet_fields")
  HashMap<String, List<Object>> facetFields;
}
