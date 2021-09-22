package com.valtech.aem.saas.api.fulltextsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Value;

/**
 * POJO representing the fulltext search results. Each result contains title and description content; and the url of the
 * page where the content is found.
 */
@Value
@Builder
public class Result {

  @JsonInclude(Include.NON_EMPTY)
  String url;

  @JsonInclude(Include.NON_EMPTY)
  String title;

  @JsonInclude(Include.NON_EMPTY)
  String description;
}
