package com.valtech.aem.saas.core.i18n;

import com.day.cq.i18n.I18n;
import lombok.NonNull;
import org.apache.sling.api.SlingHttpServletRequest;

public interface I18nProvider {

  I18n getI18n(@NonNull SlingHttpServletRequest request);
}
