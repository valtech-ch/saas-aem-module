package com.valtech.aem.saas.core.indexing;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.valtech.aem.saas.api.indexing.dto.IndexUpdateResponseDTO;
import com.valtech.aem.saas.api.indexing.IndexUpdateService;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer.JobResult;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IndexUpdateJobConsumerTest {

  @Mock
  IndexUpdateService indexUpdateService;

  @Mock
  Job job;

  @InjectMocks
  IndexUpdateJobConsumer testee;

  @BeforeEach
  void setUp() {
  }

  @Test
  void testProcess_actionMissing() {
    assertThrows(IllegalArgumentException.class, () -> testee.process(job));
  }

  @Test
  void testProcess_clientMissing() {
    mockAction(IndexUpdateAction.UPDATE.getName());
    assertThrows(IllegalArgumentException.class, () -> testee.process(job));
  }

  @Test
  void testProcess_urlMissing() {
    mockAction(IndexUpdateAction.UPDATE.getName());
    mockClient();
    assertThrows(IllegalArgumentException.class, () -> testee.process(job));
  }

  @Test
  void testProcess_repositoryPathMissing() {
    mockAction(IndexUpdateAction.UPDATE.getName());
    mockClient();
    mockUrl();
    assertThrows(IllegalArgumentException.class, () -> testee.process(job));
  }

  @Test
  void testProcess_noIllegalArguments() {
    mockAction(IndexUpdateAction.UPDATE.getName());
    mockClient();
    mockUrl();
    mockRepositoryPath();
    assertDoesNotThrow(() -> testee.process(job));
  }

  @Test
  void testProcess_indexUpdateOk() {
    mockAction(IndexUpdateAction.UPDATE.getName());
    mockClient();
    mockUrl();
    mockRepositoryPath();
    IndexUpdateResponseDTO indexUpdateResponseDto = mock(IndexUpdateResponseDTO.class);
    when(indexUpdateService.indexUrl(anyString(), anyString(), anyString())).thenReturn(
        Optional.of(indexUpdateResponseDto));
    MatcherAssert.assertThat(testee.process(job), Is.is(JobResult.OK));
  }

  @Test
  void testProcess_indexUpdateFailed() {
    mockAction(IndexUpdateAction.UPDATE.getName());
    mockClient();
    mockUrl();
    mockRepositoryPath();
    when(indexUpdateService.indexUrl(anyString(), anyString(), anyString())).thenReturn(Optional.empty());
    MatcherAssert.assertThat(testee.process(job), Is.is(JobResult.FAILED));
  }

  @Test
  void testProcess_indexDeleteOk() {
    mockAction(IndexUpdateAction.DELETE.getName());
    mockClient();
    mockUrl();
    mockRepositoryPath();
    IndexUpdateResponseDTO indexUpdateResponseDto = mock(IndexUpdateResponseDTO.class);
    when(indexUpdateService.deleteIndexedUrl(anyString(), anyString(), anyString())).thenReturn(
        Optional.of(indexUpdateResponseDto));
    MatcherAssert.assertThat(testee.process(job), Is.is(JobResult.OK));
  }

  @Test
  void testProcess_indexDeleteFailed() {
    mockAction(IndexUpdateAction.DELETE.getName());
    mockClient();
    mockUrl();
    mockRepositoryPath();
    when(indexUpdateService.deleteIndexedUrl(anyString(), anyString(), anyString())).thenReturn(Optional.empty());
    MatcherAssert.assertThat(testee.process(job), Is.is(JobResult.FAILED));
  }

  @Test
  void testProcess_fallbackProcessing() {
    mockAction("foo");
    mockClient();
    mockUrl();
    mockRepositoryPath();
    MatcherAssert.assertThat(testee.process(job), Is.is(JobResult.CANCEL));
  }

  private void mockAction(String action) {
    when(job.getProperty(IndexUpdateJobConsumer.JOB_PROPERTY_ACTION, StringUtils.EMPTY)).thenReturn(action);
  }

  private void mockClient() {
    when(job.getProperty(IndexUpdateJobConsumer.JOB_PROPERTY_CLIENT, StringUtils.EMPTY)).thenReturn("foo");
  }

  private void mockUrl() {
    when(job.getProperty(IndexUpdateJobConsumer.JOB_PROPERTY_URL, StringUtils.EMPTY)).thenReturn("bar");
  }

  private void mockRepositoryPath() {
    when(job.getProperty(IndexUpdateJobConsumer.JOB_PROPERTY_REPOSITORY_PATH, StringUtils.EMPTY)).thenReturn("baz");
  }
}
