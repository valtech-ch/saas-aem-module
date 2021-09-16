package com.valtech.aem.saas.core.query;

import static org.hamcrest.core.Is.is;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class TypeaheadSearchTextParserTest {

  @Test
  void testParser() {
    TypeaheadSearchTextParser properParser = new TypeaheadSearchTextParser("foo bar");
    MatcherAssert.assertThat(properParser.getPrefix().isPresent(), is(true));
    MatcherAssert.assertThat(properParser.getPrefix().get(), is("foo"));
    MatcherAssert.assertThat(properParser.getTerm().isPresent(), is(true));
    MatcherAssert.assertThat(properParser.getTerm().get(), is("bar"));

    TypeaheadSearchTextParser properParserWithTrailingWhiteSpaces = new TypeaheadSearchTextParser("foo  bar  ");
    MatcherAssert.assertThat(properParserWithTrailingWhiteSpaces.getPrefix().isPresent(), is(true));
    MatcherAssert.assertThat(properParserWithTrailingWhiteSpaces.getPrefix().get(), is("foo"));
    MatcherAssert.assertThat(properParserWithTrailingWhiteSpaces.getTerm().isPresent(), is(true));
    MatcherAssert.assertThat(properParserWithTrailingWhiteSpaces.getTerm().get(), is("bar"));

    TypeaheadSearchTextParser noPrefixParser = new TypeaheadSearchTextParser("foo");
    MatcherAssert.assertThat(noPrefixParser.getPrefix().isPresent(), is(false));
    MatcherAssert.assertThat(noPrefixParser.getTerm().isPresent(), is(true));
    MatcherAssert.assertThat(noPrefixParser.getTerm().get(), is("foo"));

    TypeaheadSearchTextParser improperParserEmptyText = new TypeaheadSearchTextParser("");
    MatcherAssert.assertThat(improperParserEmptyText.getPrefix().isPresent(), is(false));
    MatcherAssert.assertThat(improperParserEmptyText.getTerm().isPresent(), is(false));

    TypeaheadSearchTextParser improperParserTextNull = new TypeaheadSearchTextParser(null);
    MatcherAssert.assertThat(improperParserTextNull.getPrefix().isPresent(), is(false));
    MatcherAssert.assertThat(improperParserTextNull.getTerm().isPresent(), is(false));
  }

}
