package com.valtech.aem.saas.core.http.response;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import lombok.ToString;
import lombok.Value;

@ToString
@Value
public class ResponseHeader {

  public static final String PN_RESPONSE_HEADER = "responseHeader";
  public static final String PN_QUERY_TIME = "QTime";

  int status;

  @SerializedName(PN_QUERY_TIME)
  int queryTime;

  Map<String, String> params;
}
