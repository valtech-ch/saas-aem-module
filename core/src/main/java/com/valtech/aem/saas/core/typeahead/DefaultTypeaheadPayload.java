package com.valtech.aem.saas.core.typeahead;

import com.valtech.aem.saas.api.typeahead.TypeaheadPayload;
import java.util.Map;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class DefaultTypeaheadPayload implements TypeaheadPayload {

  String text;

  String language;

  @Singular
  Map<String, String> filterEntries;

}
