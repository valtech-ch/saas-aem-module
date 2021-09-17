package com.valtech.aem.saas.core.http.request;

import java.net.URI;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * Custom Http Delete implementation that supports request body.
 */
class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {

  public HttpDeleteWithBody() {
  }

  public HttpDeleteWithBody(final URI uri) {
    this.setURI(uri);
  }

  public HttpDeleteWithBody(final String uri) {
    this(URI.create(uri));
  }

  public String getMethod() {
    return HttpDelete.METHOD_NAME;
  }
}
