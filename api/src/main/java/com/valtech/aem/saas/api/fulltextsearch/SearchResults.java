package com.valtech.aem.saas.api.fulltextsearch;

import com.adobe.cq.export.json.ComponentExporter;
import java.util.List;

public interface SearchResults extends ComponentExporter {

  String getTerm();

  int getStartPage();

  int getResultsPerPage();

  List<Result> getResults();

  String getLoadMoreButtonText();
}
