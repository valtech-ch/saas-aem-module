package com.valtech.aem.saas.core.common.request;

import com.day.cq.wcm.api.Page;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

  public List<String> getSelectors() {
    return Stream.of(request.getRequestPathInfo().getSelectors()).collect(Collectors.toList());
  }

  public Locale getLocale() {
    return Optional.ofNullable(currentPage).map(p -> p.getLanguage(false)).orElse(Locale.getDefault());
  }

}
