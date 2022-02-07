package com.valtech.aem.saas.core.indexing;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.indexing.IndexUpdateService;
import com.valtech.aem.saas.api.indexing.dto.IndexContentPayloadDTO;
import com.valtech.aem.saas.api.indexing.dto.IndexUpdateResponseDTO;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.client.SearchAdminRequestExecutorService;
import com.valtech.aem.saas.core.http.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStreamReader;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DefaultIndexUpdateServiceTest {

    public static final String SAMPLE_URL = "https://wknd.site/us/en/adventures/bali-surf-camp.html";
    public static final String SAMPLE_REPO_PATH = "/content/wknd/(?!www)";

    @Mock
    SearchAdminRequestExecutorService searchAdminRequestExecutorService;

    IndexUpdateService testee;

    @BeforeEach
    void setUp(AemContext context) {
        context.registerInjectActivateService(new DefaultSearchServiceConnectionConfigurationService());
        context.registerService(SearchAdminRequestExecutorService.class, searchAdminRequestExecutorService);
        testee = context.registerInjectActivateService(new DefaultIndexUpdateService());
    }

    @Test
    void testIndexUrl() {
        when(searchAdminRequestExecutorService.execute(any(SearchRequest.class)))
                .thenReturn(Optional.of(new SearchResponse(getSuccessResponse(), true)));
        Optional<IndexUpdateResponseDTO> response = testee.indexUrl(SAMPLE_URL, SAMPLE_REPO_PATH);
        assertThat(response.isPresent(), is(true));
        testSuccessfulResponse(response.get());
    }

    @Test
    void testIndexUrl_exceptionDuringExecution() {
        when(searchAdminRequestExecutorService.execute(any(SearchRequest.class)))
                .thenReturn(Optional.empty());
        Optional<IndexUpdateResponseDTO> response = testee.indexUrl(SAMPLE_URL, SAMPLE_REPO_PATH);
        assertThat(response.isPresent(), is(false));
    }

    @Test
    void testIndexUrl_inputValidationFails() {
        assertThrows(IllegalArgumentException.class,
                     () -> testee.indexUrl(StringUtils.EMPTY, SAMPLE_REPO_PATH));
        assertThrows(NullPointerException.class,
                     () -> testee.indexUrl(SAMPLE_URL, null));
    }

    @Test
    void testIndexContent() {
        when(searchAdminRequestExecutorService.execute(any(SearchRequest.class)))
                .thenReturn(Optional.of(new SearchResponse(getSuccessResponse(), true)));
        IndexContentPayloadDTO indexContentPayload = getCompleteDefaultIndexContentPayload();
        Optional<IndexUpdateResponseDTO> response = testee.indexContent(indexContentPayload);
        assertThat(response.isPresent(), is(true));
        testSuccessfulResponse(response.get());
    }

    @Test
    void testIndexContent_inputValidationFails() {
        assertThrows(NullPointerException.class, () -> testee.indexContent(null));
    }

    @Test
    void testIndexContent_exceptionDuringExecution() {
        when(searchAdminRequestExecutorService.execute(any(SearchRequest.class)))
                .thenReturn(Optional.empty());
        Optional<IndexUpdateResponseDTO> response = testee.indexContent(getCompleteDefaultIndexContentPayload());
        assertThat(response.isPresent(), is(false));
    }

    @Test
    void testDeleteIndexedUrl() {
        when(searchAdminRequestExecutorService.execute(any(SearchRequest.class)))
                .thenReturn(Optional.of(new SearchResponse(getSuccessResponse(), true)));
        Optional<IndexUpdateResponseDTO> response = testee.deleteIndexedUrl(SAMPLE_URL, SAMPLE_REPO_PATH);
        assertThat(response.isPresent(), is(true));
        testSuccessfulResponse(response.get());
    }

    @Test
    void testDeleteIndexedUrl_exceptionDuringExecution() {
        when(searchAdminRequestExecutorService.execute(any(SearchRequest.class)))
                .thenReturn(Optional.empty());
        Optional<IndexUpdateResponseDTO> response = testee.deleteIndexedUrl(SAMPLE_URL, SAMPLE_REPO_PATH);
        assertThat(response.isPresent(), is(false));
    }


    @Test
    void testDeleteIndexedUrl_inputValidationFails() {
        assertThrows(IllegalArgumentException.class,
                     () -> testee.deleteIndexedUrl(StringUtils.EMPTY, SAMPLE_REPO_PATH));
        assertThrows(IllegalArgumentException.class,
                     () -> testee.deleteIndexedUrl(SAMPLE_URL, ""));
    }

    private void testSuccessfulResponse(IndexUpdateResponseDTO response) {
        assertThat(response, instanceOf(IndexUpdateResponseDTO.class));
        assertThat(response.getUrl(), is(SAMPLE_URL));
        assertThat(response.getMessage(), is("Added URL to queue of site"));
        assertThat(response.getSiteId(), is("1"));

    }

    private JsonObject getSuccessResponse() {
        return new JsonParser().parse(
                                       new InputStreamReader(getClass().getResourceAsStream("/__files/search" +
                                                                                                    "/indexupdate" +
                                                                                                    "/success.json")))
                               .getAsJsonObject();
    }

    private IndexContentPayloadDTO getCompleteDefaultIndexContentPayload() {
        return new IndexContentPayloadDTO("adventures content that is pushed.",
                                          "WKND Adventures",
                                          "https://wknd.site/us/en/adventures.html",
                                          "/content/wknd/(?!www)",
                                          "en",
                                          "sailing",
                                          "boat sailing is great",
                                          "scope");
    }
}
