package com.valtech.aem.saas.core.http.client;

import org.apache.http.*;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.protocol.HttpContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class JWTHttpRequestExecutorTest {

    @Spy
    HttpRequest httpRequest = new HttpHead();

    @Mock
    HttpClientConnection httpClientConnection;

    @Mock
    HttpResponse httpResponse;

    @Mock
    StatusLine statusLine;

    @Mock
    HttpContext httpContext;

    @Test
    void testExecute() throws HttpException, IOException {
        Mockito.when(httpClientConnection.receiveResponseHeader()).thenReturn(httpResponse);
        Mockito.when(httpResponse.getStatusLine()).thenReturn(statusLine);
        Mockito.when(statusLine.getStatusCode()).thenReturn(200);
        String token = "foo_bar";
        JWTHttpRequestExecutor testee = new JWTHttpRequestExecutor(token);
        testee.execute(httpRequest, httpClientConnection, httpContext);
        Mockito.verify(httpRequest, Mockito.times(1)).addHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token));
    }
}