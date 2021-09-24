package com.valtech.aem.saas.core.common.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.sling.api.SlingHttpServletRequest;
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
    assertThat(requestWrapper.getCurrentPage().isPresent(), is(true));
    assertThat(requestWrapper.getCurrentPage().get().getPath(), is("/foo/bar"));

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

  @Test
  void testGetI18n_fromRequestContext(AemContext context) {
    SlingHttpServletRequest request = spy(context.request());
    RequestWrapper requestWrapper = request.adaptTo(RequestWrapper.class);
    requestWrapper.getI18n();
    verify(request, never()).getResourceBundle(Locale.ENGLISH);
  }

  @Test
  void testGetI18n_fromAemResourceBundle(AemContext context) {
    context.currentPage(context.create().page("/foo/bar"));
    SlingHttpServletRequest request = spy(context.request());
    doReturn(resourceBundle).when(request).getResourceBundle(any(Locale.class));
    RequestWrapper requestWrapper = request.adaptTo(RequestWrapper.class);
    testAdaptTo(requestWrapper);
    requestWrapper.getI18n();
    verify(request, times(1)).getResourceBundle(any(Locale.class));
  }

  private void testAdaptTo(RequestWrapper requestWrapper) {
    assertThat(requestWrapper, notNullValue());
    assertThat(requestWrapper, instanceOf(RequestWrapper.class));
  }
}
