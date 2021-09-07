package com.valtech.aem.saas.core.http.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIUtils;

/**
 * Helper class for resolving {@link HttpHost} from an url string.
 */
@Slf4j
@RequiredArgsConstructor
public final class HttpHostResolver {

  private final String url;

  /**
   * Returns an optional of {@link HttpHost}.
   *
   * @return an optional present if http host can be resolved from the url string otherwise empty.
   */
  public Optional<HttpHost> getHost() {
    return Optional.ofNullable(getURI(url)).map(URIUtils::extractHost);
  }

  private URI getURI(String url) {
    try {
      if (url != null) {
        return new URI(url);
      }
    } catch (URISyntaxException e) {
      log.error("Can't determine host URI", e);
    }
    return null;
  }
}
