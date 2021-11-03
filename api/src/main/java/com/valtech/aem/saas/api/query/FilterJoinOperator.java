package com.valtech.aem.saas.api.query;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum FilterJoinOperator {
  AND(" "),
  OR(" OR ");

  @Getter
  private final String text;

}
