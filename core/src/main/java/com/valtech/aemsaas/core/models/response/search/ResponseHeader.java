package com.valtech.aemsaas.core.models.response.search;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import lombok.ToString;
import lombok.Value;

@ToString
@Value
public class ResponseHeader {

  int status;

  @SerializedName("QTime")
  int queryTime;

  Map<String, String> params;
}
