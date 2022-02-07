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
                   JobConsumer.PROPERTY_TOPICS + "=" + IndexDeleteJobConsumer.JOB_TOPIC
           })
@ServiceDescription("Search as a Service - Index Delete Job Consumer")
@Slf4j
public class IndexDeleteJobConsumer extends AbstractIndexUpdateActionJobConsumer {

    public static final String JOB_TOPIC = "com/valtech/aem/saas/indexing/jobs/indexDelete";

    @Reference
    private IndexUpdateService indexUpdateService;

    @Override
    protected JobResult processJob(String url, String repositoryPath) {
        Optional<IndexUpdateResponseDTO> response = indexUpdateService.deleteIndexedUrl(url, repositoryPath);
        if (response.isPresent()) {
            log.debug("Index delete successful: {}", response.get());
            return JobResult.OK;
        }
        return JobResult.FAILED;
    }

}
