package com.valtech.aem.saas.core.i18n;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultI18nProviderTest {

  @Mock
  Page page;

  @Mock
  SlingHttpServletRequest request;

  @Mock
  ResourceResolver resourceResolver;

  @Mock
  Resource resource;

  @Mock
  PageManager pageManager;

  @Mock
  ResourceBundle resourceBundle;

  @InjectMocks
  DefaultI18nProvider defaultI18nProvider;

  @BeforeEach
  void setUp() {
    when(request.getResourceResolver()).thenReturn(resourceResolver);
  }

  @Test
  void testGetI18n_fromRequestContext() {
    defaultI18nProvider.getI18n(request);
    verify(resourceResolver, times(1)).adaptTo(PageManager.class);
    verify(pageManager, never()).getContainingPage(resource);
  }

  @Test
  void testGetI18n_fromAemResourceBundle() {
    when(request.getResource()).thenReturn(resource);
    when(request.getResourceBundle(Locale.ENGLISH)).thenReturn(resourceBundle);
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    when(pageManager.getContainingPage(resource)).thenReturn(page);
    when(page.getLanguage(false)).thenReturn(Locale.ENGLISH);
    defaultI18nProvider.getI18n(request);
    verify(resourceResolver, times(1)).adaptTo(PageManager.class);
  }
}
