package com.valtech.aem.saas.core.http.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.valtech.aem.saas.api.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, AemContextExtension.class})
class DefaultSearchRequestExecutorServiceTest {

  @Mock
  SearchRequest searchRequest;

  @Mock
  HttpUriRequest request;

  @Mock
  HttpClientBuilderFactory httpClientBuilderFactory;

  @Mock
  HttpClientBuilder httpClientBuilder;

  @Mock
  CloseableHttpClient httpClient;

  @Mock
  CloseableHttpResponse response;

  SearchRequestExecutorService testee;

  @BeforeEach
  void setUp(AemContext context) {
    when(searchRequest.getRequest()).thenReturn(request);
    when(httpClientBuilderFactory.newBuilder()).thenReturn(httpClientBuilder);
    when(httpClientBuilder.build()).thenReturn(httpClient);
    context.registerService(HttpClientBuilderFactory.class, httpClientBuilderFactory);
    context.registerInjectActivateService(new DefaultSearchServiceConnectionConfigurationService());
    testee = context.registerInjectActivateService(new DefaultSearchRequestExecutorService());
  }

  @Test
  void testExecute_clientExecuteThrowsException() throws IOException {
    when(httpClient.execute(request)).thenThrow(IOException.class);
    assertThat(testee.execute(searchRequest).isPresent(), is(false));
  }

  @Test
  void testExecute_responsNotOK() throws IOException {
    when(searchRequest.getSuccessStatusCodes()).thenReturn(Collections.singletonList(HttpServletResponse.SC_OK));
    when(httpClient.execute(request)).thenReturn(response);
    StatusLine statusLine = Mockito.mock(StatusLine.class);
    when(response.getStatusLine()).thenReturn(statusLine);
    HttpEntity httpEntity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(httpEntity);
    when(statusLine.getStatusCode()).thenReturn(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    assertThat(testee.execute(searchRequest).isPresent(), is(false));
  }

  @Test
  void testExecute_responsNotOK_withResponseBody() throws IOException {
    when(searchRequest.getSuccessStatusCodes()).thenReturn(Collections.singletonList(HttpServletResponse.SC_OK));
    when(httpClient.execute(request)).thenReturn(response);
    StatusLine statusLine = Mockito.mock(StatusLine.class);
    when(response.getStatusLine()).thenReturn(statusLine);
    HttpEntity httpEntity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(httpEntity);
    when(httpEntity.getContent()).thenReturn(
        IOUtils.toInputStream("{\"message\":\"foo bar\"}", StandardCharsets.UTF_8.name()));
    when(statusLine.getStatusCode()).thenReturn(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    Optional<SearchResponse> searchResponse = testee.execute(searchRequest);
    assertThat(searchResponse.isPresent(), is(true));
    assertThat(searchResponse.get().isSuccess(), is(false));
  }

  @Test
  void testExecute_responsOK() throws IOException {
    when(httpClient.execute(request)).thenReturn(response);
    StatusLine statusLine = Mockito.mock(StatusLine.class);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(HttpServletResponse.SC_OK);
    HttpEntity httpEntity = new StringEntity(
        IOUtils.toString(getClass().getResourceAsStream("/__files/search/fulltext/response.json"),
            StandardCharsets.UTF_8.name()));
    when(response.getEntity()).thenReturn(httpEntity);
    assertThat(testee.execute(searchRequest).isPresent(), is(true));
  }

  @Test
  void testExecute_responsOK_invalidJsonFormat() throws IOException {
    when(httpClient.execute(request)).thenReturn(response);
    StatusLine statusLine = Mockito.mock(StatusLine.class);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(HttpServletResponse.SC_OK);
    HttpEntity httpEntity = new StringEntity("{{}");
    when(response.getEntity()).thenReturn(httpEntity);
    assertThat(testee.execute(searchRequest).isPresent(), is(false));
  }
}
