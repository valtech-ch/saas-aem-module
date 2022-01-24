package com.valtech.aem.saas.core.fulltextsearch;

import lombok.Getter;

public enum AlertVariant {
    WARNING("warning"),
    ERROR("error");

    AlertVariant(String value) {
        this.value = value;
    }

    @Getter
    private final String value;
}
