package com.valtech.aem.saas.api.tracking.dto;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;
import lombok.Value;

@ToString
@Value
public class UrlTrackingDTO {
    String createdAt;
    String id;
    @SerializedName("siteid")
    int siteId;
    String url;
}
