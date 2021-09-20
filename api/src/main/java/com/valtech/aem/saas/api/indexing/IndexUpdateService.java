package com.valtech.aem.saas.api.indexing;

import java.util.Optional;
import lombok.NonNull;

/**
 * Service for indexing content on SaaS admin.
 */
public interface IndexUpdateService {

  /**
   * Schedules an 'add index content' job to the indexing queue in saas admin.
   *
   * @param client         unique identifier assigned to a site in the saas admin tool.
   * @param url            public url of the resource presenting the content that will be indexed.
   * @param repositoryPath a regex matching the resource/content node's location in jcr.
   * @return response optional, which is empty if the there has been an error during the request execution.
   */
  Optional<IndexUpdateResponse> indexUrl(@NonNull String client, @NonNull String url, @NonNull String repositoryPath);

  /**
   * Adds an 'delete index' job to the indexing queue in saas admin.
   *
   * @param client         unique identifier assigned to a site in the saas admin tool.
   * @param url            public url of the resource presenting the content that will be removed from indexed data.
   * @param repositoryPath a regex matching the resource/content node's location in jcr.
   * @return response optional, which is empty if the there has been an error during the request execution.
   */
  Optional<IndexUpdateResponse> deleteIndexedUrl(@NonNull String client, @NonNull String url,
      @NonNull String repositoryPath);

  /**
   * Adds an 'add index content' job to the indexing queue in saas admin.
   *
   * @param client              unique identifier assigned to a site in the saas admin tool.
   * @param indexContentPayload a pojo that is used for creating a json payload with a predefined structure, that
   *                            represents the content to be indexed.
   * @return response optional, which is empty if the there has been an error during the request execution.
   */
  Optional<IndexUpdateResponse> indexContent(@NonNull String client, @NonNull IndexContentPayload indexContentPayload);

}
