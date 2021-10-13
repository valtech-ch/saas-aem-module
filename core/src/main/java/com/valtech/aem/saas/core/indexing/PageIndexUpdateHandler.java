package com.valtech.aem.saas.core.indexing;

import com.day.cq.commons.Externalizer;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationEvent;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.ImmutableMap;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.core.indexing.PageIndexUpdateHandler.Configuration;
import com.valtech.aem.saas.core.resource.ResourceResolverProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.caconfig.ConfigurationResolver;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@Component(name = "Search as a Service - Page Replication Event Handler",
    immediate = true,
    service = {EventHandler.class},
    configurationPolicy = ConfigurationPolicy.REQUIRE,
    configurationPid = "com.valtech.aem.saas.core.indexing.IndexUpdateHandler",
    property = {
        EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC,
        EventConstants.EVENT_TOPIC + "=" + ReplicationEvent.EVENT_TOPIC
    })
@Designate(ocd = Configuration.class)
@Slf4j
public class PageIndexUpdateHandler implements EventHandler {

  @Reference
  private JobManager jobManager;

  @Reference
  private ConfigurationResolver configurationResolver;

  @Reference
  private Externalizer externalizer;

  @Reference
  private ResourceResolverProvider resourceResolverProvider;

  @Reference
  private PathExternalizerPipeline pathExternalizerPipeline;

  private Configuration configuration;

  @Override
  public void handleEvent(Event event) {
    ReplicationAction action = getReplicationAction(event);
    if (action == null) {
      return;
    }
    String actionPath = action.getPath();
    log.info("Replication action {} occurred on {}", action.getType(), actionPath);
    resourceResolverProvider.resourceResolverConsumer(resourceResolver ->
        getSaasClient(resourceResolver, actionPath).ifPresent(client -> {
          Map<String, Object> propertiesProto = getPropertiesPrototype(action, client);
          if (configuration.indexUpdateHandler_enableAemExternalizer()) {
            scheduleJobForPath(externalizer.publishLink(resourceResolver, actionPath), propertiesProto);
          }
          if (configuration.indexUpdateHandler_enableCustomPathExternalizerPipeline()) {
            pathExternalizerPipeline.getExternalizedPaths(actionPath)
                .forEach(s -> scheduleJobForPath(s, propertiesProto));
          }
        }));
  }

  private Optional<String> getSaasClient(ResourceResolver resourceResolver, String pagePath) {
    return Optional.ofNullable(getContextResource(resourceResolver, pagePath))
        .map(configurationResolver::get)
        .map(configurationBuilder -> configurationBuilder.as(SearchConfiguration.class))
        .map(SearchConfiguration::client)
        .filter(StringUtils::isNotBlank);
  }

  private Resource getContextResource(ResourceResolver resourceResolver, String pagePath) {
    PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
    if (pageManager != null) {
      Page page = pageManager.getPage(pagePath);
      if (page != null) {
        return page.adaptTo(Resource.class);
      }
      log.warn("{} is not a Page", pagePath);
    }
    return null;
  }

  private ReplicationAction getReplicationAction(Event event) {
    ReplicationAction action = getAction(event);
    if (action == null) {
      log.debug("Not able to resolve a replication action from {}", event);
      return null;
    }
    if (ReplicationActionType.ACTIVATE != action.getType()
        && ReplicationActionType.DEACTIVATE != action.getType()
        && ReplicationActionType.DELETE != action.getType()) {
      log.debug("Unknown action type occurred.");
      return null;
    }
    log.debug("Handling replication action of type: {}, for resource on path: {}.", action.getType(), action.getPath());
    return action;
  }

  private ImmutableMap<String, Object> getPropertiesPrototype(ReplicationAction action,
      String client) {
    return ImmutableMap.<String, Object>builder()
        .put(IndexUpdateJobConsumer.JOB_PROPERTY_ACTION, resolveIndexUpdateAction(action.getType()).getName())
        .put(IndexUpdateJobConsumer.JOB_PROPERTY_CLIENT, client)
        .put(IndexUpdateJobConsumer.JOB_PROPERTY_REPOSITORY_PATH, action.getPath())
        .build();
  }

  private void scheduleJobForPath(String externalizedPath, Map<String, Object> propertiesPrototype) {
    Map<String, Object> properties = new HashMap<>(propertiesPrototype);
    properties.put(IndexUpdateJobConsumer.JOB_PROPERTY_URL, externalizedPath);
    List<String> errorMessages = new ArrayList<>();
    Job job = jobManager.createJob(IndexUpdateJobConsumer.INDEX_UPDATE).properties(properties).add(errorMessages);
    log.info("Added job: {}, Errors: {}", job.getId(), errorMessages);
  }

  private ReplicationAction getAction(Event event) {
    String topic = event.getTopic();
    if (ReplicationAction.EVENT_TOPIC.equals(topic)) {
      return ReplicationAction.fromEvent(event);
    } else if (ReplicationEvent.EVENT_TOPIC.equals(topic)) {
      return ReplicationEvent.fromEvent(event).getReplicationAction();
    }
    return null;
  }

  private IndexUpdateAction resolveIndexUpdateAction(ReplicationActionType replicationActionType) {
    if (ReplicationActionType.ACTIVATE == replicationActionType) {
      return IndexUpdateAction.UPDATE;
    }
    return IndexUpdateAction.DELETE;
  }

  @Activate
  @Modified
  private void activate(Configuration configuration) {
    this.configuration = configuration;
  }

  @ObjectClassDefinition(name = "Search as a Service - Index Update Event Listener",
      description = "Replication event handler that trigger SaaS index update.")
  public @interface Configuration {

    @AttributeDefinition(name = "Enable Aem Externalizer",
        description = "If enabled, an index update job, with page path externalized by the default AEM externalizer, will be scheduled.")
    boolean indexUpdateHandler_enableAemExternalizer() default false;

    @AttributeDefinition(name = "Enable Custom Path Externalizer Pipeline",
        description = "If enabled, the page path will be externalized by the pipeline consisted PathExternalizer implementations. (It is preceded by the Day CQ Externalizer.)")
    boolean indexUpdateHandler_enableCustomPathExternalizerPipeline() default false;
  }
}
