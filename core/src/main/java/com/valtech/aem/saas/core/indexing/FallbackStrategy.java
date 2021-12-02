package com.valtech.aem.saas.core.indexing;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.event.jobs.consumer.JobConsumer.JobResult;

/**
 * A fallback strategy used when no action from {@link IndexUpdateAction} is supplied. It effectively cancels the index
 * update action job.
 */
@Slf4j
@NoArgsConstructor
public class FallbackStrategy implements IndexUpdateJobProcessingStrategy {

    @Override
    public JobResult process(
            String client,
            String url,
            String repositoryPath) {
        return JobResult.CANCEL;
    }
}
