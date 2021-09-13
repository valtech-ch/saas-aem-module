package com.valtech.aem.saas.core.common.page;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Optional;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ContainingPageTest {

  Resource input;

  Page page;

  @BeforeEach
  void setUp(AemContext context) {
    page = context.create().page("/foo/bar/page", "/page/template/sample");
    input = context.create().resource(page, "resource");
  }

  @Test
  void testGetContainingPage(AemContext context) {
    Optional<Page> result = new ContainingPage(context.resourceResolver()).get(input);
    assertThat(result.isPresent(), is(true));
    assertThat(result.get(), is(page));
  }

  @Test
  void testGetContainingPage_noPageManager(AemContext context) {
    ResourceResolver resourceResolver = Mockito.mock(ResourceResolver.class);
    Mockito.when(resourceResolver.adaptTo(PageManager.class)).thenReturn(null);
    Optional<Page> result = new ContainingPage(resourceResolver).get(input);
    assertThat(result.isPresent(), is(false));
  }
}
