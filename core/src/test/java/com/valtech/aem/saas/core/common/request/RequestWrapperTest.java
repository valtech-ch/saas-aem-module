package com.valtech.aem.saas.core.common.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Locale;
import java.util.ResourceBundle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class RequestWrapperTest {

  @Mock
  ResourceBundle resourceBundle;

  @Test
  void testGetCurrentPage(AemContext context) {
    context.currentPage(context.create().page("/foo/bar"));
    RequestWrapper requestWrapper = context.request().adaptTo(RequestWrapper.class);
    testAdaptTo(requestWrapper);
    assertThat(requestWrapper.getCurrentPage(), is(notNullValue()));
    assertThat(requestWrapper.getCurrentPage().getPath(), is("/foo/bar"));

  }

  @Test
  void testGetLocale(AemContext context) {
    context.currentPage(context.create().page("/content/foo/de"));
    RequestWrapper requestWrapper = context.request().adaptTo(RequestWrapper.class);
    testAdaptTo(requestWrapper);
    assertThat(requestWrapper.getLocale(), is(Locale.GERMAN));

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
  void testGetSelectors(AemContext context) {
    context.requestPathInfo().setSelectorString("foo.bar.qux");
    RequestWrapper requestWrapper = context.request().adaptTo(RequestWrapper.class);
    testAdaptTo(requestWrapper);
    assertThat(requestWrapper.getSelectors(), not(empty()));
  }

  @Test
  void testGetSuffix_missing(AemContext context) {
    RequestWrapper requestWrapper = context.request().adaptTo(RequestWrapper.class);
    testAdaptTo(requestWrapper);
    assertThat(requestWrapper.getSuffix().isPresent(), is(false));
  }

  @Test
  void testGetSelectors_missing(AemContext context) {
    RequestWrapper requestWrapper = context.request().adaptTo(RequestWrapper.class);
    testAdaptTo(requestWrapper);
    assertThat(requestWrapper.getSelectors(), is(empty()));
  }

  private void testAdaptTo(RequestWrapper requestWrapper) {
    assertThat(requestWrapper, notNullValue());
    assertThat(requestWrapper, instanceOf(RequestWrapper.class));
  }
}
