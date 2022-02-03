package com.valtech.aem.saas.core.indexing;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;

@Slf4j
public abstract class AbstractIndexUpdateActionJobConsumer implements JobConsumer {

    public static final String JOB_PROPERTY_ACTION = "action";
    public static final String JOB_PROPERTY_URL = "url";
    public static final String JOB_PROPERTY_REPOSITORY_PATH = "repositoryPath";

    @Override
    public JobResult process(Job job) {
        String url = job.getProperty(JOB_PROPERTY_URL, StringUtils.EMPTY);
        String repositoryPath = job.getProperty(JOB_PROPERTY_REPOSITORY_PATH, StringUtils.EMPTY);
        validateJobProperty(StringUtils.isNotEmpty(url), "Url is not specified.", job);
        validateJobProperty(StringUtils.isNotEmpty(repositoryPath), "Repository path is not specified.", job);
        return processJob(url, repositoryPath);
    }

    protected abstract JobResult processJob(String url, String repositoryPath);

    private void validateJobProperty(
            boolean validation,
            String failMessage,
            Job job) {
        if (!validation) {
            log.info("Job [{}] will be canceled.", job.getId());
            //If the processing fails with throwing an exception/throwable, the process
            // will not be rescheduled and treated like the method would have returned JobConsumer.JobResult.CANCEL.
            throw new IllegalArgumentException(failMessage);
        }
    }
}
