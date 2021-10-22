package com.valtech.aem.saas.core.typeahead;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;

class TypeaheadSearchTextParserTest {

  @Test
  void testParser() {
    TypeaheadSearchTextParser properParser = new TypeaheadSearchTextParser("foo bar");
    assertThat(properParser.getPrefix().isPresent(), is(true));
    assertThat(properParser.getPrefix().get(), is("foo"));
    assertThat(properParser.getTerm().isPresent(), is(true));
    assertThat(properParser.getTerm().get(), is("bar"));

    TypeaheadSearchTextParser properParserWith3orMoreWords = new TypeaheadSearchTextParser("foo bar baz");
    assertThat(properParserWith3orMoreWords.getPrefix().isPresent(), is(true));
    assertThat(properParserWith3orMoreWords.getPrefix().get(), is("foo bar"));
    assertThat(properParserWith3orMoreWords.getTerm().isPresent(), is(true));
    assertThat(properParserWith3orMoreWords.getTerm().get(), is("baz"));

    TypeaheadSearchTextParser properParserWithTrailingWhiteSpaces = new TypeaheadSearchTextParser("foo  bar  ");
    assertThat(properParserWithTrailingWhiteSpaces.getPrefix().isPresent(), is(true));
    assertThat(properParserWithTrailingWhiteSpaces.getPrefix().get(), is("foo"));
    assertThat(properParserWithTrailingWhiteSpaces.getTerm().isPresent(), is(true));
    assertThat(properParserWithTrailingWhiteSpaces.getTerm().get(), is("bar"));

    TypeaheadSearchTextParser noPrefixParser = new TypeaheadSearchTextParser("foo");
    assertThat(noPrefixParser.getPrefix().isPresent(), is(false));
    assertThat(noPrefixParser.getTerm().isPresent(), is(true));
    assertThat(noPrefixParser.getTerm().get(), is("foo"));

    TypeaheadSearchTextParser improperParserEmptyText = new TypeaheadSearchTextParser("");
    assertThat(improperParserEmptyText.getPrefix().isPresent(), is(false));
    assertThat(improperParserEmptyText.getTerm().isPresent(), is(false));

    TypeaheadSearchTextParser improperParserTextNull = new TypeaheadSearchTextParser(null);
    assertThat(improperParserTextNull.getPrefix().isPresent(), is(false));
    assertThat(improperParserTextNull.getTerm().isPresent(), is(false));
  }

}
