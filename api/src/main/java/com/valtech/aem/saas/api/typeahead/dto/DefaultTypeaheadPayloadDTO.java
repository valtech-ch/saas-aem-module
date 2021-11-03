package com.valtech.aem.saas.api.typeahead.dto;

import com.valtech.aem.saas.api.query.Filter;
import java.util.Set;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 * A POJO that represents typeahead payload and provides instance creation.
 */
@Value
@Builder
public class DefaultTypeaheadPayloadDTO implements TypeaheadPayloadDTO {

  String text;

  String language;

  @Singular
  Set<Filter> filters;

}
