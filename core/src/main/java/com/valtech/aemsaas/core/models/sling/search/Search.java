package com.valtech.aemsaas.core.models.sling.search;

import com.adobe.cq.export.json.ContainerExporter;
import java.util.List;


public interface Search extends ContainerExporter {

  List<Filter> getFilters();

  int getResultsPerPage();

  String getSearchFieldPlaceholderText();

  String getSearchButtonText();

  String getLoadMoreButtonText();
}
