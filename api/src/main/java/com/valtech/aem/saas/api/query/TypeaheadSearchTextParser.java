package com.valtech.aem.saas.api.query;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * A helper class that parses and prepares the typeahead input text for the {@link
 * TypeaheadTextQuery}
 */
@RequiredArgsConstructor
public final class TypeaheadSearchTextParser {

    public static final String SEARCH_TEXT_DELIMITER = " ";
    public static final String REGEX_MATCHING_CONSECUTIVE_WHITESPACE_CHARS = "\\s{2,}";
    private final String text;

    public Optional<String> getTerm() {
        Optional<String> safeText = getSafeText();
        if (safeText.filter(t -> StringUtils.contains(t, SEARCH_TEXT_DELIMITER)).isPresent()) {
            safeText = safeText
                    .map(t -> StringUtils.substringAfterLast(t, SEARCH_TEXT_DELIMITER));
        }
        return safeText.map(s -> StringUtils.appendIfMissing(s, TypeaheadTextQuery.SEARCH_ALL));
    }

    public Optional<String> getPrefix() {
        return getSafeText();
    }

    private Optional<String> getSafeText() {
        return Optional.ofNullable(text)
                       .filter(StringUtils::isNotBlank)
                       .map(this::trimConsecutiveWhitespaceChars)
                       .map(StringUtils::trim);
    }

    private String trimConsecutiveWhitespaceChars(String text) {
        return text.replaceAll(REGEX_MATCHING_CONSECUTIVE_WHITESPACE_CHARS, SEARCH_TEXT_DELIMITER);
    }
}
