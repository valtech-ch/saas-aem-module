package com.valtech.aem.saas.core.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class UniqueUtilsTest {

  @Test
  void getUniqueId() {
    assertThrows(NullPointerException.class, () -> UniqueUtils.getUniqueId(null));
    assertThat(UniqueUtils.getUniqueId(""), is(""));
    assertThat(UniqueUtils.getUniqueId("foo"), is("saas_acbd18db"));
    assertThat(UniqueUtils.getUniqueId("bar"), is("saas_37b51d19"));
  }
}
