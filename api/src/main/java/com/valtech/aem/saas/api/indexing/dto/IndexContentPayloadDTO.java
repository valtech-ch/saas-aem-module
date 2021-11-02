package com.valtech.aem.saas.api.indexing.dto;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * An object representing the payload for content indexing requests. It performs input param validation when
 * constructing the object.
 */
@Getter
public class IndexContentPayloadDTO {

  public static final String PN_LANGUAGE = "language";
  public static final String PN_META_KEYWORDS = "meta_keywords";
  public static final String PN_META_DESCRIPTION = "meta_description";
  public static final String PN_SCOPE = "scope";

  private String content;

  private String title;

  private String url;

  @SerializedName("repository_path")
  private String repositoryPath;

  private JsonObject metadata;

  @SuppressWarnings("java:S107")
  public IndexContentPayloadDTO(String content, String title, String url, String repositoryPath, String language,
      String metaKeywords, String metaDescription, String scope) {
    if (StringUtils.isAnyBlank(content, title, url, repositoryPath, language, metaKeywords, metaDescription,
        scope)) {
      throw new IllegalArgumentException("Please set value for all required fields.");
    }
    this.content = content;
    this.title = title;
    this.url = url;
    this.repositoryPath = repositoryPath;
    this.metadata = getMetadata(language, metaKeywords, metaDescription, scope);
  }

  public IndexContentPayloadDTO addMetadataProperty(String name, String value) {
    if (metadata == null) {
      metadata = new JsonObject();
    }
    metadata.addProperty(name, value);
    return this;
  }

  private JsonObject getMetadata(String language, String metaKeywords, String metaDescription, String scope) {
    JsonObject metadataObject = new JsonObject();
    metadataObject.addProperty(PN_LANGUAGE, language);
    metadataObject.addProperty(PN_META_KEYWORDS, metaKeywords);
    metadataObject.addProperty(PN_META_DESCRIPTION, metaDescription);
    metadataObject.addProperty(PN_SCOPE, scope);
    return metadataObject;
  }
}
