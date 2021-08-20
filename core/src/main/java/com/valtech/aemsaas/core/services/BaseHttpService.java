package com.valtech.aemsaas.core.services;

import com.valtech.aemsaas.core.models.commons.SearchHeaders;
import com.valtech.aemsaas.core.models.responses.BaseResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CredentialsProvider;

import java.io.IOException;
import java.util.List;

public interface BaseHttpService {
    /**
     * Executes a get on a given url and returns a String if the response is 200. It returns null in any other case.
     *
     * @return String
     */
    public BaseResponse executeGet(String url, SearchHeaders headers, CredentialsProvider credentialsProvider) throws IOException;

    /**
     * Executes a post on a given url and returns a BaseResponse object
     *
     * @return BaseResponse
     */
    public BaseResponse executePost(String url, List<NameValuePair> postParameters, SearchHeaders headers, CredentialsProvider credentialsProvider) throws IOException;

    /**
     * Generates the headers object that is used to inject headers in to the request. If auth is enabled it injects
     * Authentication Basic header with the tocken generated from configuration
     *
     * @return SearchHeaders
     */
    public SearchHeaders generateSearchHeader();

    public String getWebserviceURL();

    public String getQueryParam();

    public CredentialsProvider getDefaultCredentialsProvider();
}
