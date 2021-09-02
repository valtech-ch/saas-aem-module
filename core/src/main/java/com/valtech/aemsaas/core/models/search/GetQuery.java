package com.valtech.aemsaas.core.models.search;

import java.util.List;
import org.apache.http.NameValuePair;

public interface GetQuery {

  List<NameValuePair> getEntries();
}
