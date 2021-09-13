package com.valtech.aem.saas.core.common.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetRequestWrapperTest {

  @Mock
  SlingHttpServletRequest request;

  @Test
  void testNullArgument() {
    Assertions.assertThrows(NullPointerException.class, () -> new GetRequestWrapper(null));
  }

  @Test
  void testGetRequest() {
    GetRequestWrapper wrapper = new GetRequestWrapper(request);
    assertThat(wrapper.getRequest(), instanceOf(SlingHttpServletRequestWrapper.class));
    assertThat(wrapper.getRequest().getMethod(), is(HttpConstants.METHOD_GET));

  }
}
