package com.valtech.aem.saas.core.indexing;

import com.day.cq.commons.Externalizer;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationEvent;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.PageManagerFactory;
import com.google.common.collect.ImmutableMap;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.core.indexing.IndexUpdateHandler.Configuration;
import com.valtech.aem.saas.core.resource.ResourceResolverProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationResolver;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
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
    configurationPid = "com.valtech.aem.saas.core.indexing.IndexUpdateHandler",
    property = {
        EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC,
        EventConstants.EVENT_TOPIC + "=" + ReplicationEvent.EVENT_TOPIC
    })
@Designate(ocd = Configuration.class)
@Slf4j
public class IndexUpdateHandler implements EventHandler {

  @Reference
  private JobManager jobManager;

  @Reference
  private ConfigurationResolver configurationResolver;

  @Reference
  private Externalizer externalizer;

  @Reference
  private ResourceResolverProvider resourceResolverProvider;

  @Reference
  private PageManagerFactory pageManagerFactory;

  private Configuration configuration;

  @Override
  public void handleEvent(Event event) {
    if (!configuration.enable()) {
      return;
    }
    ReplicationAction action = getAction(event);
    if (action == null) {
      return;
    }
    if (ReplicationActionType.ACTIVATE != action.getType()
        && ReplicationActionType.DEACTIVATE != action.getType()
        && ReplicationActionType.DELETE != action.getType()) {
      log.debug("Unknown action type occurred.");
      return;
    }

    String pagePath = action.getPath();
    resourceResolverProvider.resourceResolverConsumer("saas-content-reader", resourceResolver -> {
      PageManager pageManager = pageManagerFactory.getPageManager(resourceResolver);
      Page page = pageManager.getPage(pagePath);
      if (page != null) {
        Resource pageResource = page.adaptTo(Resource.class);
        if (pageResource != null) {
          SearchConfiguration searchConfiguration = configurationResolver.get(pageResource)
              .as(SearchConfiguration.class);
          log.info("Replication action {} occurred on {}", action.getType(), pagePath);
          Map<String, Object> properties = ImmutableMap.<String, Object>builder()
              .put(IndexUpdateJobConsumer.JOB_PROPERTY_ACTION, resolveIndexUpdateAction(action.getType()).name())
              .put(IndexUpdateJobConsumer.JOB_PROPERTY_CLIENT, searchConfiguration.client())
              .put(IndexUpdateJobConsumer.JOB_PROPERTY_URL, externalizer.publishLink(resourceResolver, pagePath))
              .put(IndexUpdateJobConsumer.JOB_PROPERTY_REPOSITORY_PATH, action.getPath())
              .build();
          List<String> errorMessages = new ArrayList<>();
          Job job = jobManager.createJob(IndexUpdateJobConsumer.INDEX_UPDATE).properties(properties).add(errorMessages);
          log.info("Job: {}, Errors: {}", job, errorMessages);
        } else {
          log.error("Impossible: Page is not a resource");
        }
      } else {
        log.warn("{} is not a Page", pagePath);
      }
    });
  }

  private ReplicationAction getAction(Event event) {
    String topic = event.getTopic();
    if (topic.equals(ReplicationAction.EVENT_TOPIC)) {
      return ReplicationAction.fromEvent(event);
    } else if (topic.equals(ReplicationEvent.EVENT_TOPIC)) {
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

    @AttributeDefinition(name = "Enable",
        description = "If enabled, page replication event will trigger an according index update action.")
    boolean enable() default false;
  }
}
