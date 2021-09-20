package com.valtech.aem.saas.core.common.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import java.util.Optional;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RequestConsumerTest {

  @Mock
  RequestPathInfo requestPathInfo;

  @Mock
  SlingHttpServletRequest request;

  @Mock
  ResourceResolver resourceResolver;

  @Mock
  Resource resource;

  @Mock
  PageManager pageManager;

  @Mock
  Page page;

  @Test
  void testNullArgument() {
    Assertions.assertThrows(NullPointerException.class, () -> new RequestConsumer(null));
  }

  @Test
  void testGetParameter() {
    when(request.getParameter("foo")).thenReturn("bar");
    RequestConsumer requestParameters = new RequestConsumer(request);
    assertThat(requestParameters.getParameter("foo").isPresent(), is(true));
    assertThat(requestParameters.getParameter("foo").get(), is("bar"));
    assertThat(requestParameters.getParameter("baz").isPresent(), is(false));
  }

  @Test
  void testRequestConsumer() {
    when(request.getRequestPathInfo()).thenReturn(requestPathInfo);
    when(requestPathInfo.getSuffix()).thenReturn("/foo");
    Optional<String> suffix = new RequestConsumer(request).getSuffix();
    assertThat(suffix.isPresent(), is(true));
    assertThat(suffix.get(), is("/foo"));
  }

  @Test
  void testRequestConsumer_emptySuffix() {
    when(request.getRequestPathInfo()).thenReturn(requestPathInfo);
    when(requestPathInfo.getSuffix()).thenReturn("");
    assertThat(new RequestConsumer(request).getSuffix().isPresent(), is(false));
  }

  @Test
  void testRequestConsumer_nullSuffix() {
    when(request.getRequestPathInfo()).thenReturn(requestPathInfo);
    when(requestPathInfo.getSuffix()).thenReturn(null);
    assertThat(new RequestConsumer(request).getSuffix().isPresent(), is(false));
  }

  @Test
  void testGetContainingPage() {
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(request.getResource()).thenReturn(resource);
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    when(pageManager.getContainingPage(resource)).thenReturn(page);
    Optional<Page> result = new RequestConsumer(request).getCurrentPage();
    assertThat(result.isPresent(), is(true));
    assertThat(result.get(), is(page));
  }

  @Test
  void testGetContainingPage_noPageManager() {
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(null);
    Optional<Page> result = new RequestConsumer(request).getCurrentPage();
    assertThat(result.isPresent(), is(false));
  }
}
