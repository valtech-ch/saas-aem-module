package com.valtech.aem.saas.core.fulltextsearch;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public final class FacetFilterParser {

  private static final String KEY_VALUES_SEPARATOR = ":";
  private static final String VALUES_SEPARATOR = ",";

  public FacetFilterParser(@NonNull String text) {
    this.key = StringUtils.substringBefore(text, KEY_VALUES_SEPARATOR);
    this.values = getValues(text);
  }

  @Getter
  private String key;

  @Getter
  private List<String> values;


  private List<String> getValues(String text) {
    return Optional.ofNullable(StringUtils.split(getValuesSubString(text), VALUES_SEPARATOR))
        .map(Stream::of)
        .orElse(Stream.empty())
        .collect(Collectors.toList());
  }

  private String getValuesSubString(String text) {
    return StringUtils.substringAfter(text, KEY_VALUES_SEPARATOR);
  }
}
