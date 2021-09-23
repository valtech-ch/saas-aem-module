package com.valtech.aem.saas.core.http.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class Highlighting {
  public static final String HIGHLIGHTING_TAG_NAME = "em";

  public static final String PN_HIGHLIGHTING = "highlighting";

  @SerializedName(PN_HIGHLIGHTING)
  private Map<String, Map<String, List<String>>> items;
}
