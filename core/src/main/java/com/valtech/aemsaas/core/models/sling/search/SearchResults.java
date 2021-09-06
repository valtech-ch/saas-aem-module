package com.valtech.aemsaas.core.models.sling.search;

import com.adobe.cq.export.json.ComponentExporter;
import com.valtech.aemsaas.core.models.search.results.Result;
import java.util.List;

public interface SearchResults extends ComponentExporter {

  String getTerm();

  int getStartPage();

  int getResultsPerPage();

  List<Result> getResults();

}
