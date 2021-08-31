package com.valtech.aemsaas.core.utils.search;

import com.valtech.aemsaas.core.models.search.FulltextSearchGetQuery;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Builder
public class FulltextSearchGetQueryStringConstructor extends GetQueryStringConstructor {

  @Getter(AccessLevel.PROTECTED)
  @Singular
  private List<FulltextSearchGetQuery> queries;

}
