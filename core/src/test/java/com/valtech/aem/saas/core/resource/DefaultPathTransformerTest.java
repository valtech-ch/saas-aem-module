package com.valtech.aem.saas.core.resource;

import com.day.cq.commons.Externalizer;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultPathTransformerTest {

    @Mock
    Externalizer externalizer;

    @Mock
    SlingHttpServletRequest request;

    @Mock
    ResourceResolver resourceResolver;

    @InjectMocks
    DefaultPathTransformer testee;

    @Test
    void testExternalize() {
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        testee.externalize(request, "foo");
        verify(externalizer, times(1)).publishLink(resourceResolver, "foo");
    }

    @Test
    void testExternalizeList() {
        Mockito.when(externalizer.publishLink(resourceResolver, "foo")).thenReturn("bar");
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        testee.externalizeList(request, "foo");
        verify(externalizer, times(1)).publishLink(resourceResolver, "foo");
    }

    @Test
    void testMap() {
        testee.map(request, "bar");
        verify(externalizer, times(1)).relativeLink(request, "bar");
    }
}
