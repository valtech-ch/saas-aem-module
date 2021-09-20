package com.valtech.aem.saas.core.i18n;

import com.day.cq.i18n.I18n;
import com.valtech.aem.saas.core.common.request.RequestConsumer;
import java.util.Optional;
import java.util.ResourceBundle;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;

@Slf4j
@Component(name = "Search as a Service - I18n Provider Service",
    service = I18nProvider.class)
public class DefaultI18nProvider implements I18nProvider {

  @Override
  public I18n getI18n(@NonNull SlingHttpServletRequest request) {
    return getResourceBundle(request)
        .map(I18n::new)
        .orElse(new I18n(request));
  }

  private Optional<ResourceBundle> getResourceBundle(SlingHttpServletRequest request) {
    return new RequestConsumer(request)
        .getCurrentPage()
        .map(page -> page.getLanguage(false))
        .map(request::getResourceBundle);
  }
}
