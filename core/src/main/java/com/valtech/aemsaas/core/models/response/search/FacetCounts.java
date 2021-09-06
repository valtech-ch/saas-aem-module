package com.valtech.aemsaas.core.models.response.search;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.List;
import lombok.Value;

@Value
public class FacetCounts {

  private static final String PN_FACET_FIELDS = "facet_fields";

  @SerializedName(PN_FACET_FIELDS)
  HashMap<String, List<Object>> facetFields;
}
