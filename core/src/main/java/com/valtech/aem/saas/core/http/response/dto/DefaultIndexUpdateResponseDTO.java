package com.valtech.aem.saas.core.http.response.dto;

import com.google.gson.annotations.SerializedName;
import com.valtech.aem.saas.api.indexing.dto.IndexUpdateResponseDTO;
import lombok.ToString;
import lombok.Value;

/**
 * Default implementation of {@link IndexUpdateResponseDTO} interface. The POJO is used for serializing response of
 * index update api.
 */
@Value
@ToString
public class DefaultIndexUpdateResponseDTO implements IndexUpdateResponseDTO {

  String message;

  String url;

  @SerializedName("site.id")
  String siteId;

  String id;
}
