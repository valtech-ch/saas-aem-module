package com.valtech.aem.saas.core.indexing;

import com.day.cq.replication.ReplicationAction;
import com.google.common.collect.ImmutableMap;
import com.valtech.aem.saas.api.resource.PathTransformer;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobBuilder;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.service.event.Event;

import java.util.Arrays;

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
    PathTransformer pathTransformer;

    @Mock
    Event event;

    PageIndexUpdateHandler testee;

    @BeforeEach
    void setUp() {
        context.registerService(JobManager.class, jobManager);
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
                                            .put("path", "/foo/bar")
                                            .build());
        when(jobManager.createJob(anyString())).thenReturn(jobBuilder);
        when(jobBuilder.properties(anyMap())).thenReturn(jobBuilder);
        when(jobBuilder.add(anyList())).thenReturn(job);
        when(pathTransformer.externalizeList(anyString())).thenReturn(Arrays.asList("foo", "bar"));
        testee.handleEvent(event);
        verify(pathTransformer, times(1)).externalizeList(anyString());
        verify(jobManager, times(2)).createJob(anyString());
    }
}
