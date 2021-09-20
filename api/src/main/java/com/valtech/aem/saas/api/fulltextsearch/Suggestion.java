package com.valtech.aem.saas.api.fulltextsearch;

import lombok.Value;

@Value
public class Suggestion {

  String text;

  int hits;

}
