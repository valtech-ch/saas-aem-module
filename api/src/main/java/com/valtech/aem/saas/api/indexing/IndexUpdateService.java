package com.valtech.aem.saas.api.indexing;

import com.valtech.aem.saas.api.indexing.dto.IndexContentPayloadDTO;
import com.valtech.aem.saas.api.indexing.dto.IndexUpdateResponseDTO;
import java.util.Optional;
import lombok.NonNull;
import org.apache.sling.api.resource.Resource;

/**
 * Service for indexing content on SaaS admin.
 */
public interface IndexUpdateService {

  /**
   * Schedules an 'add index content' job to the indexing queue in saas admin.
   *
   * @param context        resource that specifies the context. used for resolving the client and index parameters.
   * @param url            public url of the resource presenting the content that will be indexed.
   * @param repositoryPath a regex matching the resource/content node's location in jcr.
   * @return response optional, which is empty if the there has been an error during the request execution.
   */
  Optional<IndexUpdateResponseDTO> indexUrl(@NonNull Resource context, @NonNull String url,
      @NonNull String repositoryPath);

  /**
   * Adds an 'delete index' job to the indexing queue in saas admin.
   *
   * @param context        resource that specifies the context. used for resolving the client and index parameters.
   * @param url            public url of the resource presenting the content that will be removed from indexed data.
   * @param repositoryPath a regex matching the resource/content node's location in jcr.
   * @return response optional, which is empty if the there has been an error during the request execution.
   */
  Optional<IndexUpdateResponseDTO> deleteIndexedUrl(@NonNull Resource context, @NonNull String url,
      @NonNull String repositoryPath);

  /**
   * Adds an 'add index content' job to the indexing queue in saas admin.
   *
   * @param context                resource that specifies the context. used for resolving the client and index
   *                               parameters.
   * @param indexContentPayloadDto a pojo that is used for creating a json payload with a predefined structure, that
   *                               represents the content to be indexed.
   * @return response optional, which is empty if the there has been an error during the request execution.
   */
  Optional<IndexUpdateResponseDTO> indexContent(@NonNull Resource context,
      @NonNull IndexContentPayloadDTO indexContentPayloadDto);


  /**
   * Schedules an 'add index content' job to the indexing queue in saas admin.
   *
   * @param client         unique identifier assigned to a site in the saas admin tool.
   * @param url            public url of the resource presenting the content that will be indexed.
   * @param repositoryPath a regex matching the resource/content node's location in jcr.
   * @return response optional, which is empty if the there has been an error during the request execution.
   */
  Optional<IndexUpdateResponseDTO> indexUrl(@NonNull String client, @NonNull String url,
      @NonNull String repositoryPath);

  /**
   * Adds an 'delete index' job to the indexing queue in saas admin.
   *
   * @param client         unique identifier assigned to a site in the saas admin tool.
   * @param url            public url of the resource presenting the content that will be removed from indexed data.
   * @param repositoryPath a regex matching the resource/content node's location in jcr.
   * @return response optional, which is empty if the there has been an error during the request execution.
   */
  Optional<IndexUpdateResponseDTO> deleteIndexedUrl(@NonNull String client, @NonNull String url,
      @NonNull String repositoryPath);

  /**
   * Adds an 'add index content' job to the indexing queue in saas admin.
   *
   * @param client                 unique identifier assigned to a site in the saas admin tool.
   * @param indexContentPayloadDto a pojo that is used for creating a json payload with a predefined structure, that
   *                               represents the content to be indexed.
   * @return response optional, which is empty if the there has been an error during the request execution.
   */
  Optional<IndexUpdateResponseDTO> indexContent(@NonNull String client,
      @NonNull IndexContentPayloadDTO indexContentPayloadDto);

}
