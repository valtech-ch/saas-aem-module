package com.valtech.aem.saas.core.indexing;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationEvent;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.ImmutableMap;
import com.valtech.aem.saas.api.resource.PathTransformer;
import com.valtech.aem.saas.core.resource.ResourceResolverProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component(service = {EventHandler.class},
           immediate = true,
           configurationPolicy = ConfigurationPolicy.REQUIRE,
           property = {EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC,
                   EventConstants.EVENT_TOPIC + "=" + ReplicationEvent.EVENT_TOPIC})
@ServiceDescription("Search as a Service - Page Replication Event Handler")
@Slf4j
public class PageIndexUpdateHandler implements EventHandler {

    private static final Map<ReplicationActionType, IndexUpdateAction> replicationActionTypeToIndexUpdateAction =
            new EnumMap<>(ReplicationActionType.class);
    private static final Map<IndexUpdateAction, String> indexUpdateActionToJobTopic =
            new EnumMap<>(IndexUpdateAction.class);

    static {
        replicationActionTypeToIndexUpdateAction.put(ReplicationActionType.ACTIVATE, IndexUpdateAction.UPDATE);
        replicationActionTypeToIndexUpdateAction.put(ReplicationActionType.DEACTIVATE, IndexUpdateAction.DELETE);
        replicationActionTypeToIndexUpdateAction.put(ReplicationActionType.DELETE, IndexUpdateAction.DELETE);
        indexUpdateActionToJobTopic.put(IndexUpdateAction.UPDATE, IndexUpdateJobConsumer.JOB_TOPIC);
        indexUpdateActionToJobTopic.put(IndexUpdateAction.DELETE, IndexDeleteJobConsumer.JOB_TOPIC);
    }

    @Reference
    private JobManager jobManager;

    @Reference
    private ResourceResolverProvider resourceResolverProvider;

    @Reference
    private PathTransformer pathTransformer;

    @Override
    public void handleEvent(Event event) {
        ReplicationAction action = getReplicationAction(event);
        if (action == null) {
            return;
        }
        String actionPath = action.getPath();
        log.info("Replication action {} occurred on {}", action.getType(), actionPath);
        resourceResolverProvider.resourceResolverConsumer(resourceResolver -> {
            if (isPage(resourceResolver, actionPath)) {
                pathTransformer.externalizeList(resourceResolver, actionPath)
                               .forEach(s -> scheduleJobForPath(s, action));
            } else {
                log.warn("{} is not a Page", actionPath);
            }
        });
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
        log.debug("Handling replication action of type: {}, for resource on path: {}.",
                  action.getType(),
                  action.getPath());
        return action;
    }

    private void scheduleJobForPath(
            String externalizedPath,
            ReplicationAction action) {
        IndexUpdateAction indexUpdateAction = replicationActionTypeToIndexUpdateAction.get(action.getType());
        if (indexUpdateAction == null) {
            log.info("Not able to resolve IndexUpdateAction from {}", action.getType());
            return;
        }
        Map<String, Object> properties = ImmutableMap.<String, Object>builder()
                                                     .put(AbstractIndexUpdateActionJobConsumer.JOB_PROPERTY_REPOSITORY_PATH,
                                                          action.getPath())
                                                     .put(AbstractIndexUpdateActionJobConsumer.JOB_PROPERTY_URL,
                                                          externalizedPath)
                                                     .build();
        List<String> errorMessages = new ArrayList<>();
        String jobTopic = indexUpdateActionToJobTopic.get(indexUpdateAction);
        if (StringUtils.isBlank(jobTopic)) {
            log.info("Not able to resolve jobTopic for {}", indexUpdateAction);
            return;
        }
        log.debug("Scheduling job {}, with properties {}.", jobTopic, properties);
        Job job = jobManager.createJob(jobTopic).properties(properties).add(errorMessages);
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

    private boolean isPage(ResourceResolver resourceResolver, String pagePath) {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Page page = null;
        if (pageManager != null) {
            page = pageManager.getPage(pagePath);
        }
        return page != null;
    }
}
