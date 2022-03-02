package com.valtech.aem.saas.core.tracking;

import com.valtech.aem.saas.api.fulltextsearch.SearchModel;
import com.valtech.aem.saas.api.tracking.TrackingService;
import com.valtech.aem.saas.api.tracking.dto.UrlTrackingDTO;
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

import static com.valtech.aem.saas.core.tracking.TrackingServlet.TRACKING_EXTENSION;

@Slf4j
@Component(service = Servlet.class)
@SlingServletResourceTypes(resourceTypes = SearchModelImpl.RESOURCE_TYPE,
                           methods = HttpPost.METHOD_NAME,
                           selectors = TrackingServlet.TRACKING_SELECTOR,
                           extensions = TRACKING_EXTENSION)
public class TrackingServlet extends SlingAllMethodsServlet {

    public static final String TRACKING_SELECTOR = "tracking";
    public static final String TRACKING_EXTENSION = "html";
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

        String url = requestWrapper.getParameter(QUERY_PARAM_TRACKED_URL).orElse("");
        if (StringUtils.isEmpty(url)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        SearchModel searchModel = getSearch(request).orElseThrow(() -> new IllegalStateException(
                "Can not resolve search model."));

        if (StringUtils.isBlank(searchModel.getTrackingUrl())) {
            log.info("Tracking is not enabled. Please enable it in CA configration or search component's dialog");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Optional<UrlTrackingDTO> urlTrackingDTO = trackingService.trackUrl(url);
        if (!urlTrackingDTO.isPresent()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        log.debug("Url tracking entry is created: {}", urlTrackingDTO);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    private Optional<SearchModel> getSearch(@NonNull SlingHttpServletRequest request) {
        return Optional.ofNullable(request.adaptTo(SearchModel.class));
    }

}
