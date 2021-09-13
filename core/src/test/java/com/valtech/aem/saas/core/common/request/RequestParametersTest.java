package com.valtech.aem.saas.core.common.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

import org.apache.sling.api.SlingHttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RequestParametersTest {

  @Mock
  SlingHttpServletRequest request;

  @Test
  void testNullArgument() {
    Assertions.assertThrows(NullPointerException.class, () -> new RequestParameters(null));
  }

  @Test
  void testGetParameter() {
    Mockito.when(request.getParameter("foo")).thenReturn("bar");
    RequestParameters requestParameters = new RequestParameters(request);
    assertThat(requestParameters.getParameter("foo"), is("bar"));
    assertThat(requestParameters.getParameter("baz"), isEmptyString());
  }
}
