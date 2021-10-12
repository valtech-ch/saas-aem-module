package com.valtech.aem.saas.core.bestbets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class BestBetsApiCommonPathConstructorTest {

  @Test
  void testGetPath() {
    assertThrows(IllegalArgumentException.class, () -> new BestBetsApiCommonPathConstructor("",
        "/admin", "/api/v3"));
    assertThrows(IllegalArgumentException.class,
        () -> new BestBetsApiCommonPathConstructor("https://test-search-admin.infocentric.swiss",
            null, "/api/v3"));
    assertThrows(IllegalArgumentException.class,
        () -> new BestBetsApiCommonPathConstructor("https://test-search-admin.infocentric.swiss",
            "/admin", ""));
    BestBetsApiCommonPathConstructor testee =
        new BestBetsApiCommonPathConstructor("https://test-search-admin.infocentric.swiss",
            "/admin", "/api/v3");
    assertThrows(IllegalArgumentException.class, () -> testee.getPath(null));
    assertThrows(IllegalArgumentException.class, () -> testee.getPath(""));
    assertThat(testee.getPath("foo"), is("https://test-search-admin.infocentric.swiss/admin/foo/api/v3"));
  }
}
