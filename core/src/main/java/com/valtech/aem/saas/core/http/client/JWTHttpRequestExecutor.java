package com.valtech.aem.saas.core.http.client;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;

import java.io.IOException;

public class JWTHttpRequestExecutor extends HttpRequestExecutor {
    String jwtToken;

    public JWTHttpRequestExecutor(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public HttpResponse execute(final HttpRequest request, final HttpClientConnection conn, final HttpContext context) throws IOException, HttpException {
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
        return super.execute(request, conn, context);
    }
}
