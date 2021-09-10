package com.valtech.aem.saas.core.eventListeners;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationEvent;
import com.valtech.aem.saas.core.eventListeners.ReplicationEventListener.ReplicationEventListenerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@Component(name = "Search as a Service - Replication Event Listener",
        immediate = true,
        service = {EventHandler.class},
        configurationPid = "com.valtech.aem.saas.core.eventListeners.PublicationEventListener",
        property = {
                EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC,
                EventConstants.EVENT_TOPIC + "=" + ReplicationEvent.EVENT_TOPIC
        })
@Designate(ocd = ReplicationEventListenerConfiguration.class)
@Slf4j
public class ReplicationEventListener implements Runnable, EventHandler {

    static final String CONTENT_ROOT = "/content";

    @ObjectClassDefinition(name = "Search as a Service - Replication Event Listener", description = "Replication event listener to catch publish events and push the changes to Search as a Service.")
    public @interface ReplicationEventListenerConfiguration {
        @AttributeDefinition(name = "Enable", description = "If enabled, published page urls will be forwarded to the search service")
        boolean enable() default false;
    }

    private ReplicationEventListenerConfiguration config;

    @Override
    public void run() {
        log.info("Running Search as a Service - Replication Event Listener.");
    }

    @Override
    public void handleEvent(Event event) {
        if (!config.enable()) {
            return;
        }
        ReplicationAction action = getAction(event);
        if (action == null) {
            return;
        }
        String actionName = action.getType().getName();
        if (!StringUtils.equalsAny(actionName, ReplicationActionType.ACTIVATE.getName(), ReplicationActionType.DEACTIVATE.getName(), ReplicationActionType.DELETE.getName())) {
            log.debug("Unknown action type occurred.");
            return;
        }

        String repositoryPath = action.getPath();
        if (repositoryPath.startsWith(CONTENT_ROOT)) {
            //trigger indexing
        }
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

    protected void activate(ReplicationEventListenerConfiguration config) {
        this.config = config;
    }
}
