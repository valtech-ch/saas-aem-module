package com.valtech.aem.saas.core.indexing;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationEvent;
import com.google.common.collect.ImmutableMap;
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
        Event event = new Event(ReplicationAction.EVENT_TOPIC,
                                ImmutableMap.<String, String>builder()
                                            .put("type", "Test")
                                            .put("userId", "foo")
                                            .build());
        testee.handleEvent(event);
        verify(jobManager, never()).createJob(anyString());
    }

    @Test
    void testHandleEvent_noResourceResolverRetrieved() throws LoginException {
        testee = context.registerInjectActivateService(new PageIndexUpdateHandler());
        Event event = new Event(ReplicationEvent.EVENT_TOPIC,
                                ImmutableMap.<String, Object>builder()
                                            .put("modifications",
                                                 Collections.singletonList(ImmutableMap.<String, Object>builder()
                                                                                       .put("type",
                                                                                            ReplicationActionType.ACTIVATE)
                                                                                       .put("userId", "foo")
                                                                                       .put("time", 0L)
                                                                                       .put("paths",
                                                                                            new String[]{"/foo/bar"})
                                                                                       .build()))
                                            .build());
        testee.handleEvent(event);
        verify(jobManager, never()).createJob(anyString());
    }

    @Test
    void testHandleEvent_actionPathNotAPage(AemContext context) throws LoginException {
        testee = context.registerInjectActivateService(new PageIndexUpdateHandler());
        Event event = new Event(ReplicationAction.EVENT_TOPIC,
                                ImmutableMap.<String, String>builder()
                                            .put("type", "Activate")
                                            .put("userId", "foo")
                                            .put("path", "/foo/bar")
                                            .build());
        testee.handleEvent(event);
        verify(jobManager, never()).createJob(anyString());
    }

    @Test
    void testHandleEvent() throws LoginException {
        testee = context.registerInjectActivateService(new PageIndexUpdateHandler());
        Event event = new Event(ReplicationAction.EVENT_TOPIC,
                                ImmutableMap.<String, String>builder()
                                            .put("type", "Deactivate")
                                            .put("userId", "foo")
                                            .put("path", "/content/foo/bar")
                                            .build());
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

