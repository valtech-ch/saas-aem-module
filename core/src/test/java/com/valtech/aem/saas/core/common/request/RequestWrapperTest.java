package com.valtech.aem.saas.core.common.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class RequestWrapperTest {

  @Test
  void testGetCurrentPage(AemContext context) {
    context.currentPage(context.create().page("/foo/bar"));
    RequestWrapper requestWrapper = context.request().adaptTo(RequestWrapper.class);
    testAdaptTo(requestWrapper);
    assertThat(requestWrapper.getCurrentPage(), notNullValue());
    assertThat(requestWrapper.getCurrentPage().getPath(), is("/foo/bar"));

  }

  @Test
  void testGetParameter(AemContext context) {
    context.request().addRequestParameter("foo", "bar");
    RequestWrapper requestWrapper = context.request().adaptTo(RequestWrapper.class);
    testAdaptTo(requestWrapper);
    assertThat(requestWrapper.getParameter("foo").isPresent(), is(true));
    assertThat(requestWrapper.getParameter("foo").get(), is("bar"));
    assertThat(requestWrapper.getParameter("baz").isPresent(), is(false));
  }

  @Test
  void testGetSuffix(AemContext context) {
    context.requestPathInfo().setSuffix("foo");
    RequestWrapper requestWrapper = context.request().adaptTo(RequestWrapper.class);
    testAdaptTo(requestWrapper);
    assertThat(requestWrapper.getSuffix().isPresent(), is(true));
    assertThat(requestWrapper.getSuffix().get(), is("foo"));
  }

  @Test
  void testGetSuffix_missing(AemContext context) {
    RequestWrapper requestWrapper = context.request().adaptTo(RequestWrapper.class);
    testAdaptTo(requestWrapper);
    assertThat(requestWrapper.getSuffix().isPresent(), is(false));
  }

  private void testAdaptTo(RequestWrapper requestWrapper) {
    assertThat(requestWrapper, notNullValue());
    assertThat(requestWrapper, instanceOf(RequestWrapper.class));
  }
}
