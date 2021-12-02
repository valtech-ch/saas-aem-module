package com.valtech.aem.saas.api.query;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * It forms the typeahead query from a provided text string.
 */
public class TypeaheadTextQuery implements Query {

    public static final String KEY_TERM = "term";
    public static final String KEY_PREFIX = "prefix";
    public static final String SEARCH_ALL = "*";

    private final NameValuePair term;
    private NameValuePair prefix;

    public TypeaheadTextQuery(String searchText) {
        term = new BasicNameValuePair(KEY_TERM, SEARCH_ALL);
        Optional.ofNullable(searchText)
                .filter(StringUtils::isNotBlank)
                .ifPresent(t -> prefix = new BasicNameValuePair(KEY_PREFIX, t));
    }

    @Override
    public List<NameValuePair> getEntries() {
        if (prefix != null) {
            return Arrays.asList(term, prefix);
        }
        return Collections.emptyList();
    }

}
