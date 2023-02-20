package com.valtech.aem.saas.api.query;


import java.util.Arrays;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class SortQueryTest {

    @Test
    void testQuery() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SortQuery(StringUtils.EMPTY));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SortQuery(""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SortQuery(null, Sort.DESC));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SortQuery("", Sort.DESC));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SortQuery(Collections.emptyList()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SortQuery(Collections.singletonList(new ImmutablePair<>("", null))));
        assertThat(new SortQuery("foo").getEntries().size(), is(1));
        assertThat(new SortQuery("foo", Sort.ASC).getEntries().size(), is(1));
        assertThat(new SortQuery("foo", Sort.DESC).getEntries().size(), is(1));
        assertThat(new SortQuery(Collections.singletonList(new ImmutablePair<>("foo", Sort.ASC))).getEntries().size(), is(1));
        assertThat(new SortQuery(Collections.singletonList(new ImmutablePair<>("foo", Sort.DESC))).getEntries().size(), is(1));
        assertThat(new SortQuery(Arrays.asList(new ImmutablePair<>("foo", Sort.ASC), new ImmutablePair<>("foo2", Sort.DESC))).getEntries().size(), is(1));
    }
    
    @Test
    void testIsIllegalSortingInput(){
        SortQuery sortQuery = new SortQuery("foo");
        assertThat(sortQuery.isIllegalSortingInput(Arrays.asList(new ImmutablePair<>(null, null), new ImmutablePair<>("", null))), is(true));
        assertThat(sortQuery.isIllegalSortingInput(Arrays.asList(new ImmutablePair<>("foo", null), new ImmutablePair<>(null, Sort.DESC))), is(true));
        assertThat(sortQuery.isIllegalSortingInput(Arrays.asList(new ImmutablePair<>("foo", Sort.ASC), new ImmutablePair<>("foo2", Sort.DESC))), is(false));
        assertThat(sortQuery.isIllegalSortingInput(Arrays.asList(new ImmutablePair<>("foo", Sort.ASC), new ImmutablePair<>(null, null))), is(false));
    }
    
    @Test
    void testIsSortConfigured(){
        SortQuery sortQuery = new SortQuery("foo");
        assertThat(sortQuery.isSortConfigured(new ImmutablePair<>(null, null)), is(false));
        assertThat(sortQuery.isSortConfigured(new ImmutablePair<>(null, Sort.ASC)), is(false));
        assertThat(sortQuery.isSortConfigured(new ImmutablePair<>("foo", null)), is(false));
        assertThat(sortQuery.isSortConfigured(new ImmutablePair<>("foo", Sort.ASC)), is(true));
    }
    
    @Test
    void testGetSortQueryString() {
        SortQuery sortQuery = new SortQuery("foo");
        assertThat(sortQuery.getSortQueryString(Collections.singletonList(new ImmutablePair<>("foo", Sort.ASC))), is("foo asc"));
        assertThat(sortQuery.getSortQueryString(Arrays.asList(new ImmutablePair<>("foo", Sort.ASC), new ImmutablePair<>("foo2", Sort.DESC))), is("foo asc,foo2 desc"));
    }
}
