package com.valtech.aem.saas.core.resource;

import com.valtech.aem.saas.api.resource.PathTransformer;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Collections;
import java.util.List;

public class MockPathTransformer implements PathTransformer {

    @Override
    public List<String> externalizeList(
            SlingHttpServletRequest request,
            String path) {
        return Collections.singletonList(path);
    }

    @Override
    public List<String> externalizeList(ResourceResolver resourceResolver, String path) {
        return Collections.singletonList(path);
    }

    @Override
    public String externalize(
            SlingHttpServletRequest request,
            String path) {
        return path;
    }

    @Override
    public String externalize(ResourceResolver resourceResolver, String path) {
        return path;
    }

    @Override
    public String map(
            SlingHttpServletRequest request,
            String path) {
        return path;
    }
}
