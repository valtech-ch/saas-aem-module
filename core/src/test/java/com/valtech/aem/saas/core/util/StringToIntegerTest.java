package com.valtech.aem.saas.core.util;

import org.junit.jupiter.api.Test;

import java.util.OptionalInt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class StringToIntegerTest {

    @Test
    void testAsInt() {
        assertThat(new StringToInteger(null).asInt().isPresent(), is(false));
        assertThat(new StringToInteger("").asInt().isPresent(), is(false));
        assertThat(new StringToInteger("foo").asInt().isPresent(), is(false));
        OptionalInt properInt = new StringToInteger("1").asInt();
        assertThat(properInt.isPresent(), is(true));
        assertThat(properInt.getAsInt(), is(1));
    }
}
