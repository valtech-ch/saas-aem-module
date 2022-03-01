package com.valtech.aem.saas.core.tracking;

import com.google.gson.Gson;
import com.valtech.aem.saas.api.autocomplete.AutocompleteService;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.fulltextsearch.SearchModel;
import com.valtech.aem.saas.api.fulltextsearch.SearchTabModel;
import com.valtech.aem.saas.api.tracking.TrackingService;
import com.valtech.aem.saas.api.tracking.dto.UrlTrackingDTO;
import com.valtech.aem.saas.core.common.request.RequestWrapper;
import com.valtech.aem.saas.core.common.response.JsonResponseCommitter;
import com.valtech.aem.saas.core.fulltextsearch.SearchModelImpl;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component(service = Servlet.class)
@SlingServletResourceTypes(resourceTypes = SearchModelImpl.RESOURCE_TYPE,
                           methods = HttpPost.METHOD_NAME,
                           selectors = TrackingServlet.TRACKING_SELECTOR)
public class TrackingServlet extends SlingAllMethodsServlet {

    public static final String TRACKING_SELECTOR = "tracking";
    public static final String PARAM_TRACKED_URL = "trackedUrl";

    @Reference
    private transient TrackingService trackingService;

    @Override
    protected void doPost(@NonNull SlingHttpServletRequest request,
                          @NonNull SlingHttpServletResponse response) throws ServletException, IOException {

        SearchModel searchModel = getSearch(request.getResource()).orElseThrow(() -> new IllegalStateException(
                "Can not resolve search model."));

        if (StringUtils.isBlank(searchModel.getTrackingUrl())) {
            log.info("Tracking is not enabled. Please enable it in CA configration or search component's dialog");
            return;
        }

        RequestWrapper requestWrapper = request.adaptTo(RequestWrapper.class);
        if (requestWrapper == null) {
            throw new IllegalArgumentException("Can not adapt the request to RequestWrapper sling model.");
        }

        String url = requestWrapper.getParameter(PARAM_TRACKED_URL).orElse("");
        if (StringUtils.isEmpty(url)) {
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

    private Optional<SearchModel> getSearch(@NonNull Resource resource) {
        return Optional.ofNullable(resource.adaptTo(SearchModel.class));
    }

}
