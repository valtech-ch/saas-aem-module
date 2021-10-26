package com.valtech.aem.saas.api.indexing.dto;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

/**
 * Default implementation of the {@link IndexContentPayloadDTO} interface. It employs the builder pattern for object
 * creation and it performs an object validation when building the object.
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultIndexContentPayloadDTO implements IndexContentPayloadDTO {

  String content;

  String title;

  String url;

  @SerializedName("repository_path")
  String repositoryPath;

  JsonObject metadata;

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    public static final String PN_LANGUAGE = "language";
    public static final String PN_META_KEYWORDS = "meta_keywords";
    public static final String PN_META_DESCRIPTION = "meta_description";
    public static final String PN_SCOPE = "scope";

    private String content;
    private String title;
    private String url;
    private String repositoryPath;
    private String language;
    private String metaKeywords;
    private String metaDescription;
    private String scope;
    private Map<String, String> optionalMetaProperties;

    Builder() {
    }

    public Builder content(String content) {
      this.content = content;
      return this;
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Builder repositoryPath(String repositoryPath) {
      this.repositoryPath = repositoryPath;
      return this;
    }

    public Builder language(String language) {
      this.language = language;
      return this;
    }

    public Builder metaKeywords(String metaKeywords) {
      this.metaKeywords = metaKeywords;
      return this;
    }

    public Builder metaDescription(String metaDescription) {
      this.metaDescription = metaDescription;
      return this;
    }

    public Builder scope(String scope) {
      this.scope = scope;
      return this;
    }

    public Builder optionalMetaProperty(String name, String value) {
      if (this.optionalMetaProperties == null) {
        this.optionalMetaProperties = new HashMap<>();
      }
      this.optionalMetaProperties.put(name, value);
      return this;
    }

    public Builder optionalMetaProperties(Map<String, String> properties) {
      if (this.optionalMetaProperties == null) {
        this.optionalMetaProperties = new HashMap<>();
      }
      this.optionalMetaProperties.putAll(properties);
      return this;
    }

    public Builder clearOptionalMetaProperties() {
      if (this.optionalMetaProperties != null) {
        this.optionalMetaProperties.clear();
      }
      return this;
    }

    public DefaultIndexContentPayloadDTO build() {
      if (!isValid()) {
        throw new IllegalStateException("Please set value for all required fields.");
      }
      JsonObject metadata = new JsonObject();
      metadata.addProperty(PN_LANGUAGE, language);
      metadata.addProperty(PN_META_KEYWORDS, metaKeywords);
      metadata.addProperty(PN_META_DESCRIPTION, metaDescription);
      metadata.addProperty(PN_SCOPE, scope);
      Optional.ofNullable(optionalMetaProperties).orElse(Collections.emptyMap()).forEach(metadata::addProperty);
      return new DefaultIndexContentPayloadDTO(content, title, url, repositoryPath, metadata);
    }

    private boolean isValid() {
      return StringUtils.isNoneBlank(content, title, url, repositoryPath, language, metaKeywords, metaDescription,
          scope);
    }
  }


}
