package com.valtech.aem.saas.core.http.response.dto;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.List;
import lombok.Value;

@Value
public class FacetCountsDto {

  public static final String PN_FACET_COUNTS = "facet_counts";
  public static final String PN_FACET_FIELDS = "facet_fields";

  @SerializedName(PN_FACET_FIELDS)
  HashMap<String, List<Object>> facetFields;
}
