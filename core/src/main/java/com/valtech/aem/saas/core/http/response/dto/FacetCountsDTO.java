package com.valtech.aem.saas.core.http.response.dto;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.LinkedHashMap;
import java.util.List;

@Value
public class FacetCountsDTO {

    public static final String PN_FACET_COUNTS = "facet_counts";
    public static final String PN_FACET_FIELDS = "facet_fields";

    @SerializedName(PN_FACET_FIELDS)
    LinkedHashMap<String, List<JsonElement>> facetFields;
}
