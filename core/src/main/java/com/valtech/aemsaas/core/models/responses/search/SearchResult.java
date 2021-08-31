package com.valtech.aemsaas.core.models.responses.search;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class SearchResult {

  @SerializedName("meta_description")
  String metaDescription;

  String domain;

  String language;

  String title;

  @SerializedName("repository_path_url")
  String repositoryPathUrl;

  String url;

  String id;

  @SerializedName("[elevated]")
  boolean elevated;

}
