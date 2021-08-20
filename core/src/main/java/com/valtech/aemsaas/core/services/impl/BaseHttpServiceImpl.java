package com.valtech.aemsaas.core.services.impl;

import com.valtech.aemsaas.core.models.commons.SearchHeaders;
import com.valtech.aemsaas.core.models.responses.BaseResponse;
import com.valtech.aemsaas.core.services.BaseHttpService;
import com.valtech.aemsaas.core.services.configuration.BaseHttpServiceConfiguration;
import lombok.Cleanup;
import lombok.Getter;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;

@Component(name = "SAAS 01 BaseSearchService", immediate = true, service = BaseHttpService.class, configurationPid = "com.valtech.aemsaas.core.services.impl.BaseHttpServiceImpl")
@Designate(ocd = BaseHttpServiceConfiguration.class)
public class BaseHttpServiceImpl implements BaseHttpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseHttpServiceImpl.class);

    @Getter
    private String webserviceURL;
    @Getter
    private String queryParam;
    @Getter
    private CredentialsProvider defaultCredentialsProvider;
    @Reference
    private HttpClientBuilderFactory httpClientBuilderFactory;

    protected boolean useAuth;
    protected boolean ignoreSSL;
    private HttpClientBuilder httpClientBuilder;
    private RequestConfig requestConfig;

    @Activate
    protected void activate(BaseHttpServiceConfiguration config) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        webserviceURL = config.search_webservice_baseurl();
        String username = config.search_webservice_auth_user();
        String password = config.search_webservice_auth_password();
        useAuth = config.search_webservice_auth();
        ignoreSSL = config.search_webservice_ignoreSSL();
        queryParam = config.search_webservice_queryParam();
        if (useAuth) {
            LOGGER.debug("Initializing HttpService with authentication parameters");
            defaultCredentialsProvider = new BasicCredentialsProvider();
            defaultCredentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        }

        httpClientBuilder = httpClientBuilderFactory.newBuilder().setDefaultCredentialsProvider(defaultCredentialsProvider);
        if (ignoreSSL) {
            LOGGER.info("!!!!Initializing HttpService with ignoring SSL Certificate");
            httpClientBuilder = setIgnoredSSLHttpClientBuilder(httpClientBuilder);
        }

        int connectTimeoutInSeconds = config.search_http_connection_timeout() * 1000;
        int socketTimeoutInSeconds = config.search_http_socket_timeout() * 1000;
        requestConfig = RequestConfig.custom().
                setConnectTimeout(connectTimeoutInSeconds).
                setConnectionRequestTimeout(connectTimeoutInSeconds).
                setSocketTimeout(socketTimeoutInSeconds).
                build();
    }

    public BaseResponse executeGet(String url, SearchHeaders headers) throws IOException {
        return executeGet(url, headers, defaultCredentialsProvider);
    }

    public BaseResponse executeGet(String url, SearchHeaders headers, CredentialsProvider credentialsProvider) throws IOException {
        @Cleanup CloseableHttpClient client = createHttpClient(credentialsProvider);
        CloseableHttpResponse response = null;
        HttpGet request = new HttpGet(url);
        if (headers != null) {
            LOGGER.debug("Adding Headers to search");
            headers.createHeaders(request);
        }
        try {
            LOGGER.info("Executing GET to Search : {}", url);
            response = client.execute(request);
            String responseString = parseResponse(response);
            LOGGER.debug("{} : {}", url, responseString);
            return new BaseResponse(response.getStatusLine().getStatusCode(), responseString);
        } catch (Exception e) {
            LOGGER.error("{} ERROR GET", url, e);
            return new BaseResponse(0, e.getLocalizedMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }
    }

    public BaseResponse executePost(String url, List<NameValuePair> postParameters, SearchHeaders headers) throws IOException {
        return executePost(url, postParameters, headers, defaultCredentialsProvider);
    }

    public BaseResponse executePost(String url, List<NameValuePair> postParameters, SearchHeaders headers, CredentialsProvider credentialsProvider) throws IOException {
        CloseableHttpClient client = createHttpClient(credentialsProvider);
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            LOGGER.debug("Adding Headers to search");
            headers.createHeaders(httpPost);
        }
        try {
            LOGGER.debug("Adding post parameters to search : {}", postParameters.stream().map(NameValuePair::toString).collect(Collectors.joining(",")));
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charsets.UTF_8.name()));
            LOGGER.info("Executing POST to Search : {}", url);
            response = client.execute(httpPost);
            String responseString = parseResponse(response);
            LOGGER.debug("{} : {}", url, responseString);
            return new BaseResponse(response.getStatusLine().getStatusCode(), responseString);
        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }
    }

    private CloseableHttpClient createHttpClient(CredentialsProvider credentialsProvider) {
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        return httpClientBuilder.build();
    }

    public SearchHeaders generateSearchHeader() {
        return new SearchHeaders();
    }

    private String parseResponse(HttpResponse response) {
        try (BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent(), Charsets.UTF_8.name()))) {

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } catch (IOException e) {
            return StringUtils.EMPTY;
        }
    }

    private HttpClientBuilder setIgnoredSSLHttpClientBuilder(HttpClientBuilder httpClientBuilder) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                return true;
            }
        });
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                builder.build());
        return httpClientBuilder.setSSLSocketFactory(sslsf);
    }

}
