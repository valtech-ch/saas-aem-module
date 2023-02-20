package com.valtech.aem.saas.api.query;

import com.google.common.annotations.VisibleForTesting;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Implementation of {@link Query} that specifies a sorting according to a specific field.
 */
public final class SortQuery implements Query {

    static final String KEY = "sort";

    private final NameValuePair sortBy;

    /**
     * Constructs a sorting query.
     *
     * @param field field's name to sort by
     * @param sort  sorting order: ascending or descending.
     */
    public SortQuery(
            String field,
            Sort sort) {
        this(Collections.singletonList(new ImmutablePair<>(field, sort)));
    }

    /**
     * Constructs a sorting query with default order.
     *
     * @param field field's name to sort by
     */
    public SortQuery(String field) {
        this(field, Sort.ASC);
    }

    /**
     * Constructs a sorting query with multiple sort parameters.
     *
     * @param sortParameters a valid Set of String-Sort object Pair
     */
    public SortQuery(List<Pair<String, Sort>> sortParameters) {
        if (CollectionUtils.isEmpty(sortParameters)) {
            throw new IllegalArgumentException("At least one sort object must be passed.");
        }
        if (isIllegalSortingInput(sortParameters)) {
            throw new IllegalArgumentException("None of the sort objects are properly configured");
        }
        this.sortBy = new BasicNameValuePair(KEY, getSortQueryString(sortParameters));
    }

    @Override
    public List<NameValuePair> getEntries() {
        return Collections.singletonList(sortBy);
    }

    @VisibleForTesting
    boolean isIllegalSortingInput(List<Pair<String, Sort>> sortParameters) {
        return sortParameters.stream().noneMatch(this::isSortConfigured);
    }
    
    @VisibleForTesting
    boolean isSortConfigured(Pair<String, Sort> sortParameter) {
        return StringUtils.isNotBlank(sortParameter.getKey()) && sortParameter.getValue() != null;
    }
    
    @VisibleForTesting
    String getSortQueryString(List<Pair<String, Sort>> sortParameters) {
        return sortParameters.stream()
            .filter(this::isSortConfigured)
            .map(sortParameter -> String.format("%s %s", sortParameter.getKey(), sortParameter.getValue().getText()))
            .collect(Collectors.joining(","));
    }
}
