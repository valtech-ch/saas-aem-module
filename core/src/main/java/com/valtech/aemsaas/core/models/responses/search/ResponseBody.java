package com.valtech.aemsaas.core.models.responses.search;

import java.util.List;
import lombok.Value;

@Value
public class ResponseBody {

  int numFound;

  int start;

  List<SearchResult> docs;
}
