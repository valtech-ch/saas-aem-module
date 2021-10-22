package com.valtech.aem.saas.core.common.request;

import com.day.cq.wcm.api.Page;
import java.util.Locale;
import java.util.Optional;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RequestWrapper {

  @Self
  private SlingHttpServletRequest request;

  @Getter
  @ScriptVariable
  private Page currentPage;

  public Optional<String> getParameter(String name) {
    return Optional.ofNullable(request.getParameter(name)).filter(StringUtils::isNotBlank);
  }

  public Optional<String> getSuffix() {
    return Optional.ofNullable(request.getRequestPathInfo().getSuffix()).filter(StringUtils::isNotBlank);
  }

  public Locale getLocale() {
    return currentPage.getLanguage(false);
  }

}
