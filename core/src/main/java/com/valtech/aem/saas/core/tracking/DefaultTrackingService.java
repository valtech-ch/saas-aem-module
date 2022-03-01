package com.valtech.aem.saas.core.tracking;

import com.valtech.aem.saas.api.tracking.TrackingService;
import com.valtech.aem.saas.api.tracking.dto.UrlTrackingDTO;
import com.valtech.aem.saas.core.http.client.SearchAdminRequestExecutorService;
import com.valtech.aem.saas.core.http.request.SearchRequest;
import com.valtech.aem.saas.core.http.request.SearchRequestGet;
import com.valtech.aem.saas.core.http.request.SearchRequestPost;
import com.valtech.aem.saas.core.http.response.PojoDataExtractionStrategy;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.util.Optional;

@Slf4j
@Component(service = TrackingService.class)
@ServiceDescription("Search as a Service - Tracking Service")
@Designate(ocd = DefaultTrackingService.Configuration.class)
public class DefaultTrackingService implements TrackingService {

    @Reference
    private SearchAdminRequestExecutorService searchAdminRequestExecutorService;

    private Configuration configuration;

    @Override
    public Optional<UrlTrackingDTO> trackUrl(@NonNull String url) {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("Tracked url should not be empty.");
        }

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.addParameter("trackedUrl", url);
        SearchRequest searchRequest = new SearchRequestGet(createTrackingApiUrl() + uriBuilder.toString());
        Optional<SearchResponse> searchResponse = searchAdminRequestExecutorService.execute(searchRequest);

        return searchResponse.filter(SearchResponse::isSuccess)
                             .flatMap(response -> response.get(new PojoDataExtractionStrategy<>(UrlTrackingDTO.class)));
    }

    private String createTrackingApiUrl() {
        return StringUtils.join(searchAdminRequestExecutorService.getBaseUrl(),
                                configuration.trackingService_apiAction(),
                                configuration.trackingService_apiAction());
    }

    @Activate
    @Modified
    private void activate(Configuration configuration) {
        this.configuration = configuration;
    }

    @ObjectClassDefinition(name = "Search as a Service - Tracking Service Configuration",
                           description = "Analytics Tracking Api specific details.")
    public @interface Configuration {

        String DEFAULT_API_ACTION = "/analytics/track";
        String DEFAULT_API_VERSION_PATH = "/api/v3";  // NOSONAR

        @AttributeDefinition(name = "Api version path",
                             description = "Path designating the api version",
                             type = AttributeType.STRING)
        String trackingService_apiVersionPath() default DEFAULT_API_VERSION_PATH;  // NOSONAR

        @AttributeDefinition(name = "Api action",
                             description = "Path designating the action",
                             type = AttributeType.STRING)
        String trackingService_apiAction() default DEFAULT_API_ACTION; // NOSONAR

    }
}
