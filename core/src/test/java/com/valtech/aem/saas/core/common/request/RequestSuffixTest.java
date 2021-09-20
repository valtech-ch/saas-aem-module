package com.valtech.aem.saas.core.common.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RequestSuffixTest {

  @Mock
  SlingHttpServletRequest request;

  @Mock
  RequestPathInfo requestPathInfo;

  @Test
  void testNullArgument() {
    Assertions.assertThrows(NullPointerException.class, () -> new RequestSuffix(null));
  }

  @Test
  void testRequestSuffix() {
    when(request.getRequestPathInfo()).thenReturn(requestPathInfo);
    when(requestPathInfo.getSuffix()).thenReturn("/foo");
    Optional<String> suffix = new RequestSuffix(request).getSuffix();
    assertThat(suffix.isPresent(), is(true));
    assertThat(suffix.get(), is("/foo"));
  }

  @Test
  void testRequestSuffix_emptySuffix() {
    when(request.getRequestPathInfo()).thenReturn(requestPathInfo);
    when(requestPathInfo.getSuffix()).thenReturn("");
    assertThat(new RequestSuffix(request).getSuffix().isPresent(), is(false));
  }

  @Test
  void testRequestSuffix_nullSuffix() {
    when(request.getRequestPathInfo()).thenReturn(requestPathInfo);
    when(requestPathInfo.getSuffix()).thenReturn(null);
    assertThat(new RequestSuffix(request).getSuffix().isPresent(), is(false));
  }
}
