package com.valtech.aem.saas.core.indexing;

import com.valtech.aem.saas.api.indexing.IndexUpdateService;
import com.valtech.aem.saas.api.indexing.dto.IndexUpdateResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.event.jobs.consumer.JobConsumer.JobResult;

import java.util.Optional;

/**
 * Strategy for updating index content. It is associated with {@link IndexUpdateAction}.UPDATE. It guarantees execution
 * of the index content update via the apache sling job queue mechanism.
 */
@Slf4j
@RequiredArgsConstructor
public class UpdateStrategy implements IndexUpdateJobProcessingStrategy {

    private final IndexUpdateService indexUpdateService;

    @Override
    public JobResult process(
            String client,
            String url,
            String repositoryPath) {
        Optional<IndexUpdateResponseDTO> response = indexUpdateService.indexUrl(client, url, repositoryPath);
        if (response.isPresent()) {
            log.debug("Index update successful: {}", response.get());
            return JobResult.OK;
        }
        return JobResult.FAILED;
    }
}
