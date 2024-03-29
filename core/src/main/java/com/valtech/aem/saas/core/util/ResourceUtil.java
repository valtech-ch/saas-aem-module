package com.valtech.aem.saas.core.util;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

public class ResourceUtil {

    public static String generateId(@NonNull String prefix, @NonNull String path) {
        return StringUtils.join((Object[]) new String[]{prefix, "-", StringUtils.substring(String.valueOf(path.hashCode()),
                                                                                           0,
                                                                                           10)});
    }

    private ResourceUtil() {
        throw new UnsupportedOperationException();
    }
}
