package com.valtech.aem.saas.core.http.response.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class HighlightingDTO {

    public static final String HIGHLIGHTING_TAG_NAME = "em";

    public static final String PN_HIGHLIGHTING = "highlighting";

    @SerializedName(PN_HIGHLIGHTING)
    private Map<String, Map<String, List<String>>> items;
}
