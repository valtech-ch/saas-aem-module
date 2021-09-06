package com.valtech.aemsaas.core.models.response.search;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class SearchResult {

  private static final String PN_META_DESCRIPTION = "meta_description";
  private static final String PN_REPOSITORY_PATH_URL = "repository_path_url";
  private static final String PN_ELEVATED = "[elevated]";

  @SerializedName(PN_META_DESCRIPTION)
  private String metaDescription;

  private String domain;

  private String language;

  private String title;

  @SerializedName(PN_REPOSITORY_PATH_URL)
  private String repositoryPathUrl;

  private String url;

  private String id;

  @SerializedName(PN_ELEVATED)
  private boolean elevated;

}
