package com.valtech.aem.saas.core.http.response.dto;

import lombok.Value;

import java.util.List;

@Value
public class ResponseBodyDTO {

    public static final String PN_RESPONSE = "response";

    int numFound;

    int start;

    List<SearchResultDTO> docs;
}
