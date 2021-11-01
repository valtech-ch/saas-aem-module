package com.valtech.aem.saas.api.indexing.dto;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;
import lombok.Value;

/**
 * Represents the response of an index update request. The POJO is used for serializing response of index update api.
 */
@Value
@ToString
public class IndexUpdateResponseDTO {

  String message;

  String url;

  @SerializedName("site.id")
  String siteId;

  //TODO: change field to index (https://tracking.valtech.swiss/browse/ICSAAS-356)
  String id;
}
