package com.valtech.aem.saas.core.indexing;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationEvent;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.ImmutableMap;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.resource.PathTransformer;
import com.valtech.aem.saas.core.resource.ResourceResolverProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import java.util.*;
import java.util.function.Function;


@Component(name = "Search as a Service - Page Replication Event Handler",
        immediate = true,
        service = {EventHandler.class},
        configurationPolicy = ConfigurationPolicy.REQUIRE,
        configurationPid = PageIndexUpdateHandler.CONFIGURATION_PID,
        property = {
                EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC,
                EventConstants.EVENT_TOPIC + "=" + ReplicationEvent.EVENT_TOPIC
        })
@Slf4j
public class PageIndexUpdateHandler implements EventHandler {

    static final String CONFIGURATION_PID = "com.valtech.aem.saas.core.indexing.PageIndexUpdateHandler";

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
        resourceResolverProvider.resourceResolverFunction(resourceResolver ->
                                                                  getSaasClient(resourceResolver, actionPath))
                                .flatMap(Function.identity())
                                .ifPresent(client -> {
                                    Map<String, Object> propertiesPrototype = getPropertiesPrototype(action, client);
                                    pathTransformer.externalizeList(actionPath)
                                                   .forEach(s -> scheduleJobForPath(s, propertiesPrototype));
                                });
    }

    private Optional<String> getSaasClient(
            ResourceResolver resourceResolver,
            String pagePath) {
        return Optional.ofNullable(getContextResource(resourceResolver, pagePath))
                       .map(r -> r.adaptTo(SearchCAConfigurationModel.class))
                       .map(SearchCAConfigurationModel::getClient);
    }

    private Resource getContextResource(
            ResourceResolver resourceResolver,
            String pagePath) {
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
        log.debug("Handling replication action of type: {}, for resource on path: {}.",
                  action.getType(),
                  action.getPath());
        return action;
    }

    private ImmutableMap<String, Object> getPropertiesPrototype(
            ReplicationAction action,
            String client) {
        return ImmutableMap.<String, Object>builder()
                           .put(IndexUpdateJobConsumer.JOB_PROPERTY_ACTION,
                                resolveIndexUpdateAction(action.getType()).getName())
                           .put(IndexUpdateJobConsumer.JOB_PROPERTY_CLIENT, client)
                           .put(IndexUpdateJobConsumer.JOB_PROPERTY_REPOSITORY_PATH, action.getPath())
                           .build();
    }

    private void scheduleJobForPath(
            String externalizedPath,
            Map<String, Object> propertiesPrototype) {
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
}
