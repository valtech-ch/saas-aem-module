package com.valtech.aem.saas.api.tracking;

import com.valtech.aem.saas.api.tracking.dto.UrlTrackingDTO;
import lombok.NonNull;

import java.util.Optional;

public interface TrackingService {

    Optional<UrlTrackingDTO> trackUrl(@NonNull String url);
}
