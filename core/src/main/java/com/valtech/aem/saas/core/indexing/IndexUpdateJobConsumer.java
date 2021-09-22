package com.valtech.aem.saas.core.indexing;

import com.valtech.aem.saas.api.indexing.IndexUpdateResponse;
import com.valtech.aem.saas.api.indexing.IndexUpdateService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = JobConsumer.class, property = {
    JobConsumer.PROPERTY_TOPICS + "=" + IndexUpdateJobConsumer.INDEX_UPDATE
})
@Slf4j
public class IndexUpdateJobConsumer implements JobConsumer {

  public static final String INDEX_UPDATE = "com/valtech/aem/saas/indexing/jobs/indexUpdate";
  public static final String JOB_PROPERTY_ACTION = "action";
  public static final String JOB_PROPERTY_CLIENT = "client";
  public static final String JOB_PROPERTY_URL = "url";
  public static final String JOB_PROPERTY_REPOSITORY_PATH = "repositoryPath";

  @Reference
  private IndexUpdateService indexUpdateService;

  @Override
  public JobResult process(Job job) {
    String action = job.getProperty(JOB_PROPERTY_ACTION, StringUtils.EMPTY);
    String client = job.getProperty(JOB_PROPERTY_CLIENT, StringUtils.EMPTY);
    String url = job.getProperty(JOB_PROPERTY_URL, StringUtils.EMPTY);
    String repositoryPath = job.getProperty(JOB_PROPERTY_REPOSITORY_PATH, StringUtils.EMPTY);
    validateJobProperty(StringUtils.isNotEmpty(action), "Index update action is not specified.", job);
    validateJobProperty(StringUtils.isNotEmpty(client), "SaaS client identifier is not specified.", job);
    validateJobProperty(StringUtils.isNotEmpty(url), "Url is not specified.", job);
    validateJobProperty(StringUtils.isNotEmpty(repositoryPath), "Repository path is not specified.", job);
    if (IndexUpdateAction.UPDATE.name().equals(action)) {
      Optional<IndexUpdateResponse> response = indexUpdateService.indexUrl(client, url, repositoryPath);
      if (response.isPresent()) {
        log.debug("Index update successful: {}", response.get());
        return JobResult.OK;
      }
    }
    if (IndexUpdateAction.DELETE.name().equals(action)) {
      Optional<IndexUpdateResponse> response = indexUpdateService.deleteIndexedUrl(client, url, repositoryPath);
      if (response.isPresent()) {
        log.debug("Index delete successful: {}", response.get());
        return JobResult.OK;
      }
    }
    return JobResult.FAILED;
  }


  private void validateJobProperty(boolean validation, String failMessage, Job job) {
    if (!validation) {
      log.info("Job [{}] will be canceled.", job.getId());
      //If the processing fails with throwing an exception/throwable, the process
      // will not be rescheduled and treated like the method would have returned JobConsumer.JobResult.CANCEL.
      throw new IllegalArgumentException(failMessage);
    }
  }
}
