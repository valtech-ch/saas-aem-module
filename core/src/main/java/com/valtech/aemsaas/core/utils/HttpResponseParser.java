package com.valtech.aemsaas.core.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

@Slf4j
@RequiredArgsConstructor
public final class HttpResponseParser {

  private final HttpResponse response;

  public String getContentString() {
    try {
      return IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8.name());
    } catch (IOException e) {
      log.error("IOException occurred while trying to parse the response {}", response);
    }
    return StringUtils.EMPTY;
  }
}
