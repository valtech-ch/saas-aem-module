package com.valtech.aem.saas.api.tracking;

import com.valtech.aem.saas.api.tracking.dto.SearchResultItemTrackingDTO;
import lombok.NonNull;

import java.util.Optional;

/**
 * Service that consumes apis that track web user activity
 */
public interface TrackingService {

    /**
     * Creates entry for a redirect to a referenced url
     * @param url the reference
     * @return entry details
     */
    Optional<SearchResultItemTrackingDTO> trackUrl(@NonNull String url);
}
