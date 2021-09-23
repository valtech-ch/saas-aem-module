package com.valtech.aem.saas.core.indexing;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.day.cq.commons.Externalizer;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.ImmutableMap;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.core.resource.ResourceResolverProviderService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Arrays;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.caconfig.ConfigurationResolver;
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

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class IndexUpdateHandlerTest {

  private final AemContext context = new AemContext(ResourceResolverType.NONE);

  @Mock
  JobManager jobManager;

  @Mock
  JobBuilder jobBuilder;

  @Mock
  Job job;

  @Mock
  ConfigurationResolver configurationResolver;

  @Mock
  ConfigurationBuilder configurationBuilder;

  @Mock
  SearchConfiguration searchConfiguration;

  @Mock
  Externalizer externalizer;

  @Mock
  ResourceResolverFactory resourceResolverFactory;

  @Mock
  ResourceResolver resourceResolver;

  @Mock
  PageManager pageManager;

  @Mock
  Page page;

  @Mock
  Resource pageResource;

  @Mock
  PathExternalizerPipeline pathExternalizerPipeline;

  @Mock
  Event event;

  IndexUpdateHandler testee;

  @BeforeEach
  void setUp() {
    context.registerService(JobManager.class, jobManager);
    context.registerService(ConfigurationResolver.class, configurationResolver);
    context.registerService(Externalizer.class, externalizer);
    context.registerService(ResourceResolverFactory.class, resourceResolverFactory);
    context.registerInjectActivateService(new ResourceResolverProviderService());
    context.registerService(PathExternalizerPipeline.class, pathExternalizerPipeline);
  }

  @Test
  void testHandleEvent_handlerDisabled() {
    testee = context.registerInjectActivateService(new IndexUpdateHandler());
    testee.handleEvent(event);
    verify(jobManager, never()).createJob(anyString());
  }

  @Test
  void testHandleEvent_eventTopicInvalid() {
    testee = context.registerInjectActivateService(new IndexUpdateHandler(),
        ImmutableMap.<String, Object>builder()
            .put("indexUpdateHandler.enable", true)
            .build());
    testee.handleEvent(event);
    verify(jobManager, never()).createJob(anyString());
  }

  @Test
  void testHandleEvent_eventActionTypeInvalid() {
    testee = context.registerInjectActivateService(new IndexUpdateHandler(),
        ImmutableMap.<String, Object>builder()
            .put("indexUpdateHandler.enable", true)
            .build());
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
    testee = context.registerInjectActivateService(new IndexUpdateHandler(),
        ImmutableMap.<String, Object>builder()
            .put("indexUpdateHandler.enable", true)
            .build());
    Event event = new Event(ReplicationAction.EVENT_TOPIC,
        ImmutableMap.<String, String>builder()
            .put("type", "Activate")
            .put("userId", "foo")
            .put("path", "/foo/bar")
            .build());
    when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenThrow(LoginException.class);
    testee.handleEvent(event);
    verify(jobManager, never()).createJob(anyString());
  }

  @Test
  void testHandleEvent_actionPathNotAPage(AemContext context) throws LoginException {
    testee = context.registerInjectActivateService(new IndexUpdateHandler(),
        ImmutableMap.<String, Object>builder()
            .put("indexUpdateHandler.enable", true)
            .build());
    Event event = new Event(ReplicationAction.EVENT_TOPIC,
        ImmutableMap.<String, String>builder()
            .put("type", "Activate")
            .put("userId", "foo")
            .put("path", "/foo/bar")
            .build());
    when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    testee.handleEvent(event);
    verify(jobManager, never()).createJob(anyString());
  }

  @Test
  void testHandleEvent_noContextResource(AemContext context) throws LoginException {
    testee = context.registerInjectActivateService(new IndexUpdateHandler(),
        ImmutableMap.<String, Object>builder()
            .put("indexUpdateHandler.enable", true)
            .build());
    Event event = new Event(ReplicationAction.EVENT_TOPIC,
        ImmutableMap.<String, String>builder()
            .put("type", "Activate")
            .put("userId", "foo")
            .put("path", "/foo/bar")
            .build());
    when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    when(pageManager.getPage("/foo/bar")).thenReturn(page);
    testee.handleEvent(event);
    verify(jobManager, never()).createJob(anyString());
  }

  @Test
  void testHandleEvent_noSaasClientConfig(AemContext context) throws LoginException {
    testee = context.registerInjectActivateService(new IndexUpdateHandler(),
        ImmutableMap.<String, Object>builder()
            .put("indexUpdateHandler.enable", true)
            .build());
    Event event = new Event(ReplicationAction.EVENT_TOPIC,
        ImmutableMap.<String, String>builder()
            .put("type", "Activate")
            .put("userId", "foo")
            .put("path", "/foo/bar")
            .build());
    when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    when(pageManager.getPage("/foo/bar")).thenReturn(page);
    when(page.adaptTo(Resource.class)).thenReturn(pageResource);
    when(configurationResolver.get(pageResource)).thenReturn(configurationBuilder);
    when(configurationBuilder.as(SearchConfiguration.class)).thenReturn(searchConfiguration);
    when(searchConfiguration.client()).thenReturn("");
    testee.handleEvent(event);
    verify(jobManager, never()).createJob(anyString());
  }

  @Test
  void testHandleEvent_withClient_noExternalizerHookEnabled() throws LoginException {
    testee = context.registerInjectActivateService(new IndexUpdateHandler(),
        ImmutableMap.<String, Object>builder()
            .put("indexUpdateHandler.enable", true)
            .build());
    Event event = new Event(ReplicationAction.EVENT_TOPIC,
        ImmutableMap.<String, String>builder()
            .put("type", "Activate")
            .put("userId", "foo")
            .put("path", "/foo/bar")
            .build());
    when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    when(pageManager.getPage("/foo/bar")).thenReturn(page);
    when(page.adaptTo(Resource.class)).thenReturn(pageResource);
    when(configurationResolver.get(pageResource)).thenReturn(configurationBuilder);
    when(configurationBuilder.as(SearchConfiguration.class)).thenReturn(searchConfiguration);
    when(searchConfiguration.client()).thenReturn("foo");
    testee.handleEvent(event);
    verify(jobManager, never()).createJob(anyString());
  }

  @Test
  void testHandleEvent_withClient_aemExternalizerEnabled() throws LoginException {
    testee = context.registerInjectActivateService(new IndexUpdateHandler(),
        ImmutableMap.<String, Object>builder()
            .put("indexUpdateHandler.enable", true)
            .put("indexUpdateHandler.enableAemExternalizer", true)
            .build());
    Event event = new Event(ReplicationAction.EVENT_TOPIC,
        ImmutableMap.<String, String>builder()
            .put("type", "Activate")
            .put("userId", "foo")
            .put("path", "/foo/bar")
            .build());
    when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    when(pageManager.getPage("/foo/bar")).thenReturn(page);
    when(page.adaptTo(Resource.class)).thenReturn(pageResource);
    when(configurationResolver.get(pageResource)).thenReturn(configurationBuilder);
    when(configurationBuilder.as(SearchConfiguration.class)).thenReturn(searchConfiguration);
    when(searchConfiguration.client()).thenReturn("foo");
    when(jobManager.createJob(anyString())).thenReturn(jobBuilder);
    when(jobBuilder.properties(anyMap())).thenReturn(jobBuilder);
    when(jobBuilder.add(anyList())).thenReturn(job);
    testee.handleEvent(event);
    verify(externalizer, times(1)).publishLink(any(ResourceResolver.class), anyString());
    verify(jobManager, times(1)).createJob(anyString());
    verify(pathExternalizerPipeline, never()).getExternalizedPaths(anyString());
  }

  @Test
  void testHandleEvent_withClient_customPathExternalizerPipelineEnabled() throws LoginException {
    testee = context.registerInjectActivateService(new IndexUpdateHandler(),
        ImmutableMap.<String, Object>builder()
            .put("indexUpdateHandler.enable", true)
            .put("indexUpdateHandler.enableCustomPathExternalizerPipeline", true)
            .build());
    Event event = new Event(ReplicationAction.EVENT_TOPIC,
        ImmutableMap.<String, String>builder()
            .put("type", "Activate")
            .put("userId", "foo")
            .put("path", "/foo/bar")
            .build());
    when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    when(pageManager.getPage("/foo/bar")).thenReturn(page);
    when(page.adaptTo(Resource.class)).thenReturn(pageResource);
    when(configurationResolver.get(pageResource)).thenReturn(configurationBuilder);
    when(configurationBuilder.as(SearchConfiguration.class)).thenReturn(searchConfiguration);
    when(searchConfiguration.client()).thenReturn("foo");
    when(jobManager.createJob(anyString())).thenReturn(jobBuilder);
    when(jobBuilder.properties(anyMap())).thenReturn(jobBuilder);
    when(jobBuilder.add(anyList())).thenReturn(job);
    when(pathExternalizerPipeline.getExternalizedPaths(anyString())).thenReturn(Arrays.asList("foo", "bar"));
    testee.handleEvent(event);
    verify(externalizer, never()).publishLink(any(ResourceResolver.class), anyString());
    verify(pathExternalizerPipeline, times(1)).getExternalizedPaths(anyString());
    verify(jobManager, times(2)).createJob(anyString());
  }
}
