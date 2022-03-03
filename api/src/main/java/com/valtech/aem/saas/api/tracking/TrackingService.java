package com.valtech.aem.saas.api.tracking;

import com.valtech.aem.saas.api.tracking.dto.SearchResultItemTrackingDTO;
import lombok.NonNull;

import java.util.Optional;

public interface TrackingService {

    Optional<SearchResultItemTrackingDTO> trackUrl(@NonNull String url);
}
