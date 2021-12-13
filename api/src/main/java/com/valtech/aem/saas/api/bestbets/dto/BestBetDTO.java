package com.valtech.aem.saas.api.bestbets.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

/**
 * Represents a best bet details object
 */
@Value
public class BestBetDTO {

    public static final String PN_PROJECT_ID = "project_id";

    int id;

    String language;

    String term;

    String url;

    @SerializedName(PN_PROJECT_ID)
    int projectId;

    String index;
}
