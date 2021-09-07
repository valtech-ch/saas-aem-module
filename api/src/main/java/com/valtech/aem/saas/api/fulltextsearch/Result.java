package com.valtech.aem.saas.api.fulltextsearch;

import lombok.Builder;
import lombok.Value;

/**
 * POJO representing the fulltext search results. Each result contains title and description content; and the url of the
 * page where the content is found.
 */
@Value
@Builder
public class Result {

  String url;
  String title;
  String description;
}
