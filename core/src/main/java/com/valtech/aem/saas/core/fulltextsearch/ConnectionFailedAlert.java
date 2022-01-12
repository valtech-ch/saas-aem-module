package com.valtech.aem.saas.core.fulltextsearch;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@RequiredArgsConstructor
public class ConnectionFailedAlert {
    AlertVariant variant;
    List<String> messages;
}
