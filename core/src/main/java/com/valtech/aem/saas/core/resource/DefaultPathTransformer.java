package com.valtech.aem.saas.core.resource;

import com.day.cq.commons.Externalizer;
import com.valtech.aem.saas.api.resource.PathTransformer;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import java.util.Collections;
import java.util.List;

/**
 * Default implementation of {@link PathTransformer} interface, that utilizes Day CQ Link Externalizer.
 */
@Slf4j
@Component(service = PathTransformer.class)
@ServiceDescription("Search as a Service - Default Path Transformer Service")
public class DefaultPathTransformer implements PathTransformer {

    public static final String HTML_EXTENSION = ".html";

    @Reference
    private Externalizer externalizer;

    @Override
    public List<String> externalizeList(
            SlingHttpServletRequest request,
            String path) {
        return Collections.singletonList(externalize(request, path));
    }

    @Override
    public List<String> externalizeList(ResourceResolver resourceResolver, String path) {
        return Collections.singletonList(externalize(resourceResolver, path));
    }

    @Override
    public String externalize(
            SlingHttpServletRequest request,
            String path) {
        return externalize(request.getResourceResolver(), path);
    }

    @Override
    public String externalize(ResourceResolver resourceResolver, String path) {
        return externalizer.publishLink(resourceResolver, path) + HTML_EXTENSION;
    }

    @Override
    public String map(
            SlingHttpServletRequest request,
            String path) {
        return externalizer.relativeLink(request, path);
    }
}
