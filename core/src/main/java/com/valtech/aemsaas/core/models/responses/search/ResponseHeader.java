package com.valtech.aemsaas.core.models.responses.search;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import lombok.Value;

@Value
public class ResponseHeader {

  int status;

  @SerializedName("QTime")
  int qTime;

  Map<String, String> params;
}
