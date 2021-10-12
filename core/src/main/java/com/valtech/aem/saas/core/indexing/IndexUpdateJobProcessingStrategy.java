package com.valtech.aem.saas.core.indexing;

import org.apache.sling.event.jobs.consumer.JobConsumer;

/**
 * Represents a job processing strategy.
 */
public interface IndexUpdateJobProcessingStrategy {

  /**
   * Returns the result of processing an index update job.
   *
   * @param client         unique identifier assigned to a site in the saas admin tool.
   * @param url            public url of the resource presenting the content that will be indexed.
   * @param repositoryPath a regex matching the resource/content node's location in jcr.
   * @return an enum of type @{@link JobConsumer.JobResult}.
   */
  JobConsumer.JobResult process(String client, String url, String repositoryPath);
}
