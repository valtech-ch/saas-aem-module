package com.valtech.aem.saas.api.query;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link Query} that, when used, disables the highlighting feature.
 */
public final class HighlightingDisableQuery implements OptionalQuery {

    private static final String HIGHLIGHT_SWITCH = "hl";

    @Override
    public List<NameValuePair> getEntries() {
        return Collections.singletonList(new BasicNameValuePair(HIGHLIGHT_SWITCH, Boolean.FALSE.toString()));
    }
}
