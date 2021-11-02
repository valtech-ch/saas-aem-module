package com.valtech.aem.saas.core.bestbets.dto;

import com.google.gson.annotations.SerializedName;
import com.valtech.aem.saas.api.bestbets.dto.BestBetDTO;
import lombok.Value;

@Value
public class DefaultBestBetDTO implements BestBetDTO {

  public static final String PN_PROJECT_ID = "project_id";

  int id;

  String language;

  String term;

  String url;

  @SerializedName(PN_PROJECT_ID)
  int projectId;

  int identifier;
}
