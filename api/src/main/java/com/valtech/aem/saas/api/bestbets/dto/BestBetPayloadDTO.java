package com.valtech.aem.saas.api.bestbets.dto;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents the payload object for best bets api. It performs input param validation in class's constructor.
 */
@ToString
@Getter
public class BestBetPayloadDTO {

    String url;
    String term;
    String language;
    String index;

    public BestBetPayloadDTO(
            String url,
            String term,
            String language) {
        if (StringUtils.isAnyBlank(url, term, language)) {
            throw new IllegalArgumentException("All payload properties must not be blank.");
        }
        this.url = url;
        this.term = term;
        this.language = language;
    }

    public BestBetPayloadDTO index(String index) {
        this.index = index;
        return this;
    }
}
