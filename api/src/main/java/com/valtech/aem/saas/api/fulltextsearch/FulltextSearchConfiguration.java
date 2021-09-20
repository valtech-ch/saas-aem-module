package com.valtech.aem.saas.api.fulltextsearch;

/**
 * Represents a full text configuration wrapper.
 *
 * @param <T> the type of object that is subjected to configuration
 */
public interface FulltextSearchConfiguration<T> {

  /**
   * Applies configurations to the passed object.
   *
   * @param t the object that is subjected to configuration.
   * @return the same object that is passed with a configuration action applied.
   */
  T apply(T t);
}
