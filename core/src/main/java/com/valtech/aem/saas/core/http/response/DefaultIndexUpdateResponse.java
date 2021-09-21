package com.valtech.aem.saas.core.http.response;

import com.google.gson.annotations.SerializedName;
import com.valtech.aem.saas.api.indexing.IndexUpdateResponse;
import lombok.Value;

/**
 * Default implementation of {@link IndexUpdateResponse} interface. The POJO is used for serializing response of index
 * update api.
 */
@Value
public class DefaultIndexUpdateResponse implements IndexUpdateResponse {

  String message;

  String url;

  @SerializedName("site.id")
  String siteId;

  String id;
}
