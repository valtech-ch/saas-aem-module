package com.valtech.aem.saas.core.indexing;

import org.apache.sling.event.jobs.consumer.JobConsumer;

public interface IndexUpdateJobProcessingStrategy {

  JobConsumer.JobResult process(String client, String url, String repositoryPath);
}
