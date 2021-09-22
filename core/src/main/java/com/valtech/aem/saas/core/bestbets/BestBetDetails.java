package com.valtech.aem.saas.core.bestbets;

import com.google.gson.annotations.SerializedName;
import com.valtech.aem.saas.api.bestbets.BestBet;
import lombok.Value;

@Value
public class BestBetDetails implements BestBet {

  public static final String PN_PROJECT_ID = "project_id";
  int id;
  String language;

  String term;

  String url;

  @SerializedName(PN_PROJECT_ID)
  int projectId;

  int identifier;
}
