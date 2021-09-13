package com.valtech.aem.saas.core.util;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class StreamUtils {

  private StreamUtils() {
    throw new UnsupportedOperationException();
  }

  public static <T> Stream<T> asStream(Iterator<T> sourceIterator) {
    return asStream(sourceIterator, false);
  }

  public static <T> Stream<T> asStream(Iterator<T> sourceIterator, boolean parallel) {
    if (sourceIterator == null) {
      return Stream.empty();
    } else {
      Iterable<T> iterable = () -> sourceIterator;
      return StreamSupport.stream(iterable.spliterator(), parallel);
    }
  }
}
