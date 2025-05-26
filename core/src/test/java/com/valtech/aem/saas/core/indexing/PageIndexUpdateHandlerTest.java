package com.valtech.aem.saas.core.indexing;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationEvent;
import com.valtech.aem.saas.api.resource.PathTransformer;
import com.valtech.aem.saas.core.resource.ResourceResolverProviderService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobBuilder;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.service.event.Event;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class PageIndexUpdateHandlerTest {

    private final AemContext context = new AemContext(ResourceResolverType.NONE);

    @Mock
    JobManager jobManager;

    @Mock
    JobBuilder jobBuilder;

    @Mock
    Job job;

    @Mock
    ResourceResolverFactory resourceResolverFactory;

    @Mock
    ResourceResolver resourceResolver;

    @Mock
    PathTransformer pathTransformer;

    @Mock
    Event event;

    PageIndexUpdateHandler testee;

    @BeforeEach
    void setUp() {
        context.registerService(JobManager.class, jobManager);
        context.registerService(ResourceResolverFactory.class, resourceResolverFactory);
        context.registerInjectActivateService(new ResourceResolverProviderService());
        context.registerService(PathTransformer.class, pathTransformer);
    }

    @Test
    void testHandleEvent_eventTopicInvalid() {
        testee = context.registerInjectActivateService(new PageIndexUpdateHandler());
        testee.handleEvent(event);
        verify(jobManager, never()).createJob(anyString());
    }

    @Test
    void testHandleEvent_eventActionTypeInvalid() {
        testee = context.registerInjectActivateService(new PageIndexUpdateHandler());
        Map<String, String> properties = new HashMap<>();
        properties.put("type", "Test");
        properties.put("userId", "foo");
        Event event = new Event(ReplicationAction.EVENT_TOPIC, properties);
        testee.handleEvent(event);
        verify(jobManager, never()).createJob(anyString());
    }

    @Test
    void testHandleEvent_noResourceResolverRetrieved() throws LoginException {
        testee = context.registerInjectActivateService(new PageIndexUpdateHandler());
        Map<String, Object> subProperties = new HashMap<>();
        subProperties.put("type", ReplicationActionType.ACTIVATE);
        subProperties.put("userId", "foo");
        subProperties.put("time", 0L);
        subProperties.put("paths", new String[]{"/foo/bar"});
        Map<String, Object> properties = new HashMap<>();
        properties.put("modifications", Collections.singletonList(subProperties));
        Event event = new Event(ReplicationEvent.EVENT_TOPIC, properties);
        testee.handleEvent(event);
        verify(jobManager, never()).createJob(anyString());
    }

    @Test
    void testHandleEvent_actionPathNotAPage(AemContext context) throws LoginException {
        testee = context.registerInjectActivateService(new PageIndexUpdateHandler());
        Map<String, String> properties = new HashMap<>();
        properties.put("type", "Activate");
        properties.put("userId", "foo");
        properties.put("path", "/foo/bar");
        Event event = new Event(ReplicationAction.EVENT_TOPIC, properties);
        testee.handleEvent(event);
        verify(jobManager, never()).createJob(anyString());
    }

    @Test
    void testHandleEvent() throws LoginException {
        testee = context.registerInjectActivateService(new PageIndexUpdateHandler());
        Map<String, String> properties = new HashMap<>();
        properties.put("type", "Deactivate");
        properties.put("userId", "foo");
        properties.put("path", "/content/foo/bar");
        Event event = new Event(ReplicationAction.EVENT_TOPIC, properties);
        when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);
        when(jobManager.createJob(anyString())).thenReturn(jobBuilder);
        when(jobBuilder.properties(anyMap())).thenReturn(jobBuilder);
        when(jobBuilder.add(anyList())).thenReturn(job);
        when(pathTransformer.externalizeList(Mockito.eq(resourceResolver), anyString())).thenReturn(Arrays.asList("foo",
                                                                                                                  "bar"));
        testee.handleEvent(event);
        verify(pathTransformer, times(1)).externalizeList(Mockito.eq(resourceResolver), anyString());
        verify(jobManager, times(2)).createJob(anyString());
    }
}

