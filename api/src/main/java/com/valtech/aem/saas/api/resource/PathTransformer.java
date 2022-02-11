package com.valtech.aem.saas.api.resource;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

/**
 * Service interface for resource path mapping and externalizing.
 */
public interface PathTransformer {

    /**
     * Returns one or more externalized URLs for the passed path argument. It returns more than one URL in case a path can
     * be represented by more than one url (e.g. for indexing). In the core implementation a path to an AEM page will be
     * appended with the html extension and the Day CQ Link externalizer is used. For custom rewriting requirements, e.g.
     * extensionless URLs, a custom PathTransformer which higher service ranking than 0 is required.
     *
     * @param request sling request used for context.
     * @param path    resource/page path to be externalized.
     * @return list of externalized paths
     */
    List<String> externalizeList(
            SlingHttpServletRequest request,
            String path);

    /**
     * Returns one or more externalized URLs for the passed path argument. It returns more than one URL in case a
     * path can
     * be represented by more than one url (e.g. for indexing). In the core implementation a path to an AEM page will be
     * appended with the html extension and the Day CQ Link externalizer is used. For custom rewriting requirements,
     * e.g.
     * extensionless URLs, a custom PathTransformer which higher service ranking than 0 is required.
     *
     * @param resourceResolver resource resolver used for context.
     * @param path             resource/page path to be externalized.
     * @return list of externalized paths
     */
    List<String> externalizeList(
            ResourceResolver resourceResolver,
            String path);

    /**
     * Returns a URL for the passed path argument. In the core implementation a path to an AEM page will be appended
     * with
     * the html extension and the Day CQ Link externalizer is used. For custom rewriting requirements, e.g.
     * extensionless
     * URL, a custom PathTransformer which higher service ranking than 0 is required.
     *
     * @param request sling request used for context.
     * @param path    resource/page path to be externalized.
     * @return externalized path
     */
    String externalize(
            SlingHttpServletRequest request,
            String path);

    /**
     * Returns a URL for the passed path argument. In the core implementation a path to an AEM page will be appended
     * with
     * the html extension and the Day CQ Link externalizer is used. For custom rewriting requirements, e.g.
     * extensionless
     * URL, a custom PathTransformer which higher service ranking than 0 is required.
     *
     * @param resourceResolver resource resolver used for context.
     * @param path             resource/page path to be externalized.
     * @return externalized path
     */
    String externalize(
            ResourceResolver resourceResolver,
            String path);

    /**
     * Retrieves a path that is a result of the applied mapping rules. By default, the implementation uses
     * resourceResolver::map. If customization is required a higher service ranking than 0 should be used
     *
     * @param request sling request used for context.
     * @param path    the resource's path on which the mapping rules are applied.
     * @return mapped path.
     */
    String map(
            SlingHttpServletRequest request,
            String path);
}
