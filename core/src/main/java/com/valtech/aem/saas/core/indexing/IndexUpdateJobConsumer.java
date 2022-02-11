package com.valtech.aem.saas.core.indexing;

import com.valtech.aem.saas.api.indexing.IndexUpdateService;
import com.valtech.aem.saas.api.indexing.dto.IndexUpdateResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import java.util.Optional;

@Component(service = JobConsumer.class,
           configurationPolicy = ConfigurationPolicy.REQUIRE,
           property = {
                   JobConsumer.PROPERTY_TOPICS + "=" + IndexUpdateJobConsumer.JOB_TOPIC
           })
@ServiceDescription("Search as a Service - Index Update Job Consumer")
@Slf4j
public class IndexUpdateJobConsumer extends AbstractIndexUpdateActionJobConsumer {

    public static final String JOB_TOPIC = "com/valtech/aem/saas/indexing/jobs/indexUpdate";

    @Reference
    private IndexUpdateService indexUpdateService;

    @Override
    protected JobResult processJob(String url, String repositoryPath) {
        log.debug("Processing 'Index Update' Job with url: {} and repository path: {}", url, repositoryPath);
        Optional<IndexUpdateResponseDTO> response = indexUpdateService.indexUrl(url, repositoryPath);
        if (response.isPresent()) {
            log.debug("Index update successful: {}", response.get());
            return JobResult.OK;
        }
        log.debug("Index update request to SaaS has failed.");
        return JobResult.FAILED;
    }
}
