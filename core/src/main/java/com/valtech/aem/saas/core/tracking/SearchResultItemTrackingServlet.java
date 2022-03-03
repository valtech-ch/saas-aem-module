package com.valtech.aem.saas.core.tracking;

import com.valtech.aem.saas.api.fulltextsearch.SearchModel;
import com.valtech.aem.saas.api.tracking.TrackingService;
import com.valtech.aem.saas.api.tracking.dto.SearchResultItemTrackingDTO;
import com.valtech.aem.saas.core.common.request.RequestWrapper;
import com.valtech.aem.saas.core.fulltextsearch.SearchModelImpl;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.valtech.aem.saas.core.tracking.SearchResultItemTrackingServlet.SEARCH_RESULT_ITEM_TRACKING_EXTENSION;

@Slf4j
@Component(service = Servlet.class)
@SlingServletResourceTypes(resourceTypes = SearchModelImpl.RESOURCE_TYPE,
                           methods = HttpPost.METHOD_NAME,
                           selectors = SearchResultItemTrackingServlet.SEARCH_RESULT_ITEM_TRACKING_SELECTOR,
                           extensions = SEARCH_RESULT_ITEM_TRACKING_EXTENSION)
public class SearchResultItemTrackingServlet extends SlingAllMethodsServlet {

    public static final String SEARCH_RESULT_ITEM_TRACKING_SELECTOR = "tracking";
    public static final String SEARCH_RESULT_ITEM_TRACKING_EXTENSION = "html";
    public static final String QUERY_PARAM_TRACKED_URL = "trackedUrl";

    @Reference
    private transient TrackingService trackingService;

    @Override
    protected void doPost(@NonNull SlingHttpServletRequest request,
                          @NonNull SlingHttpServletResponse response) throws ServletException, IOException {

        RequestWrapper requestWrapper = request.adaptTo(RequestWrapper.class);
        if (requestWrapper == null) {
            throw new IllegalArgumentException("Can not adapt the request to RequestWrapper sling model.");
        }

        Optional<String> url = requestWrapper.getParameter(QUERY_PARAM_TRACKED_URL).filter(StringUtils::isNotBlank);
        if (!url.isPresent()) {
            log.warn("Url is not specified.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        SearchModel searchModel = getSearch(request).orElseThrow(() -> new IllegalStateException(
                "Can not resolve search model."));

        if (StringUtils.isBlank(searchModel.getTrackingUrl())) {
            log.warn("Tracking is not enabled. Please enable it in CA configuration or search component's dialog");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Optional<SearchResultItemTrackingDTO> urlTrackingDTO = trackingService.trackUrl(url.get());
        if (!urlTrackingDTO.isPresent()) {
            log.error("Failed to update tracking entry for url: {}", url.get());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        log.debug("Url tracking entry is created/updated: {}", urlTrackingDTO);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    private Optional<SearchModel> getSearch(@NonNull SlingHttpServletRequest request) {
        return Optional.ofNullable(request.adaptTo(SearchModel.class));
    }

}
