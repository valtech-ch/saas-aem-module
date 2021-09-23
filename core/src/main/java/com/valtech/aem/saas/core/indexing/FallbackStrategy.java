package com.valtech.aem.saas.core.indexing;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.event.jobs.consumer.JobConsumer.JobResult;

@Slf4j
@NoArgsConstructor
public class FallbackStrategy implements IndexUpdateJobProcessingStrategy {

  @Override
  public JobResult process(String client, String url, String repositoryPath) {
    return JobResult.CANCEL;
  }
}
