package com.valtech.aem.saas.core.typeahead;

import com.valtech.aem.saas.api.fulltextsearch.Filter;
import com.valtech.aem.saas.api.typeahead.TypeaheadPayload;
import java.util.Set;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class DefaultTypeaheadPayload implements TypeaheadPayload {

  String text;

  String language;

  @Singular
  Set<Filter> filters;

}
