package com.valtech.aem.saas.core.util;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

class StreamUtilsTest {

    @Test
    void testAsStream() {
        assertThat(StreamUtils.asStream(null), instanceOf(Stream.class));
        assertThat(StreamUtils.asStream(null).count(), is(0L));
        assertThat(StreamUtils.asStream(Collections.singletonList("foo").iterator()), instanceOf(Stream.class));
        assertThat(StreamUtils.asStream(Collections.singletonList("foo").iterator()).count(), is(1L));
    }
}
