package com.valtech.aem.saas.core.indexing;

import com.valtech.aem.saas.api.indexing.IndexUpdateService;
import com.valtech.aem.saas.api.indexing.dto.IndexUpdateResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer.JobResult;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IndexDeleteJobConsumerTest {

    @Mock
    IndexUpdateService indexUpdateService;

    @Mock
    Job job;

    @InjectMocks
    IndexDeleteJobConsumer testee;

    @Test
    void testProcess_urlMissing() {
        assertThrows(IllegalArgumentException.class, () -> testee.process(job));
    }

    @Test
    void testProcess_repositoryPathMissing() {
        mockUrl();
        assertThrows(IllegalArgumentException.class, () -> testee.process(job));
    }

    @Test
    void testProcess_noIllegalArguments() {
        mockUrl();
        mockRepositoryPath();
        assertDoesNotThrow(() -> testee.process(job));
    }

    @Test
    void testProcess_indexDeleteOk() {
        mockUrl();
        mockRepositoryPath();
        IndexUpdateResponseDTO indexUpdateResponseDto = new IndexUpdateResponseDTO("foo", "bar", "baz", "quz");
        when(indexUpdateService.deleteIndexedUrl(anyString(), anyString())).thenReturn(
                Optional.of(indexUpdateResponseDto));
        MatcherAssert.assertThat(testee.process(job), Is.is(JobResult.OK));
    }

    @Test
    void testProcess_indexDeleteFailed() {
        mockUrl();
        mockRepositoryPath();
        when(indexUpdateService.deleteIndexedUrl(anyString(), anyString())).thenReturn(Optional.empty());
        MatcherAssert.assertThat(testee.process(job), Is.is(JobResult.FAILED));
    }

    private void mockUrl() {
        when(job.getProperty(IndexUpdateJobConsumer.JOB_PROPERTY_URL, StringUtils.EMPTY)).thenReturn("bar");
    }

    private void mockRepositoryPath() {
        when(job.getProperty(IndexUpdateJobConsumer.JOB_PROPERTY_REPOSITORY_PATH, StringUtils.EMPTY)).thenReturn("baz");
    }
}
