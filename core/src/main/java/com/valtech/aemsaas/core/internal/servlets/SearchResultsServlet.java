package com.valtech.aemsaas.core.internal.servlets;

import com.day.cq.wcm.api.Page;
import com.valtech.aemsaas.core.internal.models.sling.search.SearchImpl;
import com.valtech.aemsaas.core.utils.page.ContainingPage;
import com.valtech.aemsaas.core.utils.request.GetRequestWrapper;
import com.valtech.aemsaas.core.utils.resource.ResourceChildren;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Slf4j
@Component(service = Servlet.class)
@SlingServletResourceTypes(
    resourceTypes = SearchResultsServlet.RESOURCE_TYPE_CQ_PAGE,
    methods = HttpConstants.METHOD_GET,
    selectors = SearchResultsServlet.RESULTS_SELECTOR,
    extensions = SearchResultsServlet.JSON)
public class SearchResultsServlet extends SlingSafeMethodsServlet {

  static final String JSON = "json";
  static final String RESULTS_SELECTOR = "saas-results";
  static final String RESOURCE_TYPE_CQ_PAGE = "cq/Page";
  public static final String JSON_EXPORTER_SELECTOR = "model";

  @Reference
  private ServletResolver servletResolver;

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    RequestDispatcherOptions requestDispatcherOptions = new RequestDispatcherOptions();
    requestDispatcherOptions.setReplaceSelectors(JSON_EXPORTER_SELECTOR);
    requestDispatcherOptions.setReplaceSuffix(StringUtils.EMPTY);
    Optional<Resource> searchResultsComponentResource = getSearchComponentResource(request);
    if (searchResultsComponentResource.isPresent()) {
//      SlingHttpServletResponse searchResponse = new ServletInternalRequest(servletResolver, searchResultsComponentResource.get())
//          .withSelectors(JSON_EXPORTER_SELECTOR)
//          .execute()
//          .getResponse().getOutputStream();
//
//      IOUtils.cop
//      if (HttpServletResponse.SC_OK == searchResponse.getStatus()) {
//searchResponse.getContent
//      }
      RequestDispatcher requestDispatcher = request.getRequestDispatcher(searchResultsComponentResource.get(),
          requestDispatcherOptions);
      if (requestDispatcher != null) {
        requestDispatcher.forward(new GetRequestWrapper(request).getRequest(), response);
      }
    }
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }

  private Optional<Resource> getSearchComponentResource(SlingHttpServletRequest request) {
    return new ContainingPage(request.getResourceResolver())
        .get(request.getResource())
        .map(page -> findSearchComponentResourceInPage(request.getResourceResolver(), page));
  }

  private Resource findSearchComponentResourceInPage(@NonNull ResourceResolver resourceResolver, @NonNull Page page) {
    List<Resource> resources = Optional.ofNullable(page.getContentResource())
        .map(ResourceChildren::new)
        .map(ResourceChildren::getDescendents)
        .orElse(Stream.empty())
        .filter(resource -> resourceResolver.isResourceType(resource, SearchImpl.RESOURCE_TYPE))
        .collect(Collectors.toList());
    if (resources.isEmpty()) {
      log.warn("Search component resource is not found in {}", page.getPath());
      return null;
    }
    if (resources.size() > 1) {
      log.warn("More than one search component has been found in {}", page.getPath());
    }
    log.info("Found search component resource path: {}", resources.get(0).getPath());
    return resources.get(0);
  }
}
