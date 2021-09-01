package com.valtech.aemsaas.core.models.response.search;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class SearchResult {

  @SerializedName("meta_description")
  private String metaDescription;

  private String domain;

  private String language;

  private String title;

  @SerializedName("repository_path_url")
  private String repositoryPathUrl;

  private String url;

  private String id;

  @SerializedName("[elevated]")
  private boolean elevated;

}
