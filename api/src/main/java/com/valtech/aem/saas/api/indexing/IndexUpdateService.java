package com.valtech.aem.saas.api.indexing;

import com.valtech.aem.saas.api.indexing.dto.IndexContentPayloadDTO;
import com.valtech.aem.saas.api.indexing.dto.IndexUpdateResponseDTO;
import lombok.NonNull;

import java.util.Optional;

/**
 * Service for indexing content on SaaS admin.
 */
public interface IndexUpdateService {

    /**
     * Schedules an 'add index content' job to the indexing queue in saas admin.
     *
     * @param url            public url of the resource presenting the content that will be indexed.
     * @param repositoryPath the resource/content node's location in jcr.
     * @return response optional, which is empty if the there has been an error during the request execution.
     */
    Optional<IndexUpdateResponseDTO> indexUrl(@NonNull String url, @NonNull String repositoryPath);

    /**
     * Adds an 'delete index' job to the indexing queue in saas admin.
     *
     * @param url            public url of the resource presenting the content that will be removed from indexed
     *                       data.
     * @param repositoryPath the resource/content node's location in jcr.
     * @return response optional, which is empty if the there has been an error during the request execution.
     */
    Optional<IndexUpdateResponseDTO> deleteIndexedUrl(@NonNull String url, @NonNull String repositoryPath);

    /**
     * Adds an 'add index content' job to the indexing queue in saas admin.
     *
     * @param indexContentPayloadDto a pojo that is used for creating a json payload with a predefined structure, that
     *                               represents the content to be indexed.
     * @return response optional, which is empty if the there has been an error during the request execution.
     */
    Optional<IndexUpdateResponseDTO> indexContent(@NonNull IndexContentPayloadDTO indexContentPayloadDto);

}
