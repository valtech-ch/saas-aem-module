package com.valtech.aem.saas.core.http.response.dto;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import lombok.ToString;
import lombok.Value;

import java.util.Map;

@ToString
@Value
public class ResponseHeaderDTO {

    public static final String PN_RESPONSE_HEADER = "responseHeader";
    public static final String PN_QUERY_TIME = "QTime";

    int status;

    @SerializedName(PN_QUERY_TIME)
    int queryTime;

    Map<String, JsonElement> params;
}
