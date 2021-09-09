package com.valtech.aem.saas.api.fulltextsearch;

import com.adobe.cq.export.json.ContainerExporter;
import java.util.List;


public interface Search extends ContainerExporter {

  int DEFAULT_START_PAGE = 0;
  int DEFAULT_RESULTS_PER_PAGE = 10;

  String getTerm();

  List<Filter> getFilters();

  int getResultsPerPage();

  String getSearchFieldPlaceholderText();

  String getSearchButtonText();

  String getLoadMoreButtonText();
}
