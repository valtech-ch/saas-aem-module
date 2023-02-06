package com.valtech.aem.saas.api.query;

import org.apache.commons.lang3.StringUtils;

import lombok.NoArgsConstructor;

/**
 * An implementation of Filter that represents a not filter query entry.
 */
@NoArgsConstructor
public class NotFilter extends SimpleFilter {

	private static final String FILTER_NOT_KEYWORD = "NOT ";

	public NotFilter(String name, String value, boolean startsWith) {
		super(name, value, startsWith);
	}

	@Override
	public String getQueryString() {
		if (StringUtils.isNoneBlank(name, value)) {
		return StringUtils.join(FILTER_NOT_KEYWORD, super.getQueryString());
		}
        return StringUtils.EMPTY;
	}

}
