package com.valtech.aem.saas.api.fulltextsearch.dto;

import lombok.Builder;
import lombok.Value;

/**
 * Value object representing the fulltext search results. Each result contains title and description content; and the
 * url of the page where the content is found.
 */
@Value
@Builder
public class ResultDTO {

    String url;

    String title;

    String description;

    boolean bestBet;
}
