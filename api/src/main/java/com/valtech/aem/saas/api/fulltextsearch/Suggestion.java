package com.valtech.aem.saas.api.fulltextsearch;

import lombok.Value;

/**
 * Represent search suggestion details.
 */
@Value
public class Suggestion {

  String text;

  int hits;

}
