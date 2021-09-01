package com.valtech.aemsaas.core.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class BaseResponse {
    @Getter
    private int code;

    @Getter
    private String responseData;
}
