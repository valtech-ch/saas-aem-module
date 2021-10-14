package com.valtech.aem.saas.core.indexing;

import com.valtech.aem.saas.api.indexing.IndexUpdateResponse;
import com.valtech.aem.saas.api.indexing.IndexUpdateService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.event.jobs.consumer.JobConsumer.JobResult;

/**
 * Strategy for deleting index content. It is associated with {@link IndexUpdateAction}.DELETE. It guarantees execution
 * of the index content delete via the apache sling job queue mechanism.
 */
@Slf4j
@RequiredArgsConstructor
public class DeleteStrategy implements IndexUpdateJobProcessingStrategy {

  private final IndexUpdateService indexUpdateService;

  @Override
  public JobResult process(String client, String url, String repositoryPath) {
    Optional<IndexUpdateResponse> response = indexUpdateService.deleteIndexedUrl(client, url, repositoryPath);
    if (response.isPresent()) {
      log.debug("Index delete successful: {}", response.get());
      return JobResult.OK;
    }
    return JobResult.FAILED;
  }
}
