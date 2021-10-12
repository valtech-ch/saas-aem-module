package com.valtech.aem.saas.core.bestbets;

import com.google.gson.Gson;
import com.valtech.aem.saas.api.bestbets.BestBet;
import com.valtech.aem.saas.api.bestbets.BestBetPayload;
import com.valtech.aem.saas.api.bestbets.BestBetsActionFailedException;
import com.valtech.aem.saas.api.bestbets.BestBetsConsumerService;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.request.SearchRequest;
import com.valtech.aem.saas.core.http.request.SearchRequestDelete;
import com.valtech.aem.saas.core.http.request.SearchRequestGet;
import com.valtech.aem.saas.core.http.request.SearchRequestPost;
import com.valtech.aem.saas.core.http.request.SearchRequestPut;
import com.valtech.aem.saas.core.http.response.BestBetsDataExtractionStrategy;
import com.valtech.aem.saas.core.http.response.JsonObjectDataExtractionStrategy;
import com.valtech.aem.saas.core.http.response.ModifiedBestBetIdExtractionStrategy;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;

@Builder
public class ClientBestBetsConsumerService implements BestBetsConsumerService {

  public static final String FAILED_REQUEST_EXECUTION = "Failed to execute request to best bets api.";
  private final SearchRequestExecutorService searchRequestExecutorService;
  private final String commonPath;
  private final String addBestBetAction;
  private final String addBestBetsAction;
  private final String getBestBetsAction;
  private final String updateBestBetAction;
  private final String deleteBestBetAction;
  private final String publishBestBetsForProjectAction;

  @Override
  public void addBestBet(@NonNull BestBetPayload bestBetPayload) {
    validateCommonConfigs();
    if (StringUtils.isBlank(addBestBetAction)) {
      throw new IllegalStateException("Add best bet action path is not specified.");
    }
    SearchRequest searchRequest = SearchRequestPost.builder()
        .uri(commonPath + addBestBetAction)
        .httpEntity(createJsonPayloadEntity(bestBetPayload))
        .build();
    Optional<SearchResponse> searchResponse = searchRequestExecutorService.execute(searchRequest);
    handleFailedRequestExecution(searchResponse);
    searchResponse
        .ifPresent(r -> handleSearchResponseError(r, String.format("Failed to add best bet: %s", bestBetPayload)));
  }

  @Override
  public void addBestBets(@NonNull List<BestBetPayload> bestBetPayloadList) {
    validateCommonConfigs();
    if (StringUtils.isBlank(addBestBetsAction)) {
      throw new IllegalStateException("Add best bets action path is not specified.");
    }
    SearchRequest searchRequest = SearchRequestPost.builder()
        .uri(commonPath + addBestBetsAction)
        .httpEntity(createJsonPayloadEntity(bestBetPayloadList))
        .build();
    Optional<SearchResponse> searchResponse = searchRequestExecutorService.execute(searchRequest);
    handleFailedRequestExecution(searchResponse);
    searchResponse
        .ifPresent(r -> handleSearchResponseError(r,
            String.format("Failed to add best bets: %s", bestBetPayloadList)));
  }

  @Override
  public void updateBestBet(int bestBetId, @NonNull BestBetPayload bestBetPayload) {
    validateCommonConfigs();
    if (StringUtils.isBlank(updateBestBetAction)) {
      throw new IllegalStateException("Update best bet action path is not specified.");
    }
    SearchRequest searchRequest = SearchRequestPut.builder()
        .uri(commonPath + updateBestBetAction + "/" + bestBetId)
        .httpEntity(createJsonPayloadEntity(bestBetPayload))
        .build();
    Optional<SearchResponse> searchResponse = searchRequestExecutorService.execute(searchRequest);
    handleFailedRequestExecution(searchResponse);
    searchResponse.ifPresent(r -> handleSearchResponseError(r,
        String.format("Failed to update best bet with id %s, with %s", bestBetId, bestBetPayload)));
    if (searchResponse
        .flatMap(response -> response.get(new ModifiedBestBetIdExtractionStrategy()))
        .isPresent()) {
      throw new BestBetsActionFailedException(
          String.format("Failed to update best bet: %s with the following update details %s", bestBetId,
              bestBetPayload));
    }
  }

  @Override
  public void deleteBestBet(int bestBetId) {
    validateCommonConfigs();
    if (StringUtils.isBlank(deleteBestBetAction)) {
      throw new IllegalStateException("Delete best bet action path is not specified.");
    }
    SearchRequest searchRequest = SearchRequestDelete.builder()
        .uri(commonPath + deleteBestBetAction + "/" + bestBetId)
        .build();
    Optional<SearchResponse> searchResponse = searchRequestExecutorService.execute(searchRequest);
    handleFailedRequestExecution(searchResponse);
    searchResponse.ifPresent(r -> handleSearchResponseError(r,
        String.format("Failed to delete best bet with id %s", bestBetId)));
    if (!searchResponse
        .filter(SearchResponse::isSuccess)
        .flatMap(response -> response.get(new ModifiedBestBetIdExtractionStrategy()))
        .isPresent()) {
      throw new BestBetsActionFailedException(String.format("Failed to delete best bet: %s", bestBetId));
    }
  }

  @Override
  public void publishBestBetsForProject(int projectId) {
    validateCommonConfigs();
    if (StringUtils.isBlank(publishBestBetsForProjectAction)) {
      throw new IllegalStateException("Publish best bets for project action path is not specified.");
    }
    if (!StringUtils.contains(publishBestBetsForProjectAction, "%s")) {
      throw new IllegalArgumentException(
          "Publish Best Bets For Project Action is of illegal format. It should contain a wildcard/placeholder for project id.");
    }
    SearchRequest searchRequest = new SearchRequestGet(getPreparePublishBestBetsAction(projectId));
    Optional<SearchResponse> searchResponse = searchRequestExecutorService.execute(searchRequest);
    handleFailedRequestExecution(searchResponse);
    searchResponse.ifPresent(
        r -> handleSearchResponseError(r, String.format("Failed to publish best bets for project: %s", projectId)));
  }

  @Override
  public List<BestBet> getBestBets() {
    validateCommonConfigs();
    if (StringUtils.isBlank(getBestBetsAction)) {
      throw new IllegalStateException("Get best bets action path is not specified.");
    }
    SearchRequest searchRequest = new SearchRequestGet(commonPath + getBestBetsAction);
    Optional<SearchResponse> searchResponse = searchRequestExecutorService.execute(searchRequest);
    handleFailedRequestExecution(searchResponse);
    return searchRequestExecutorService.execute(searchRequest)
        .filter(SearchResponse::isSuccess)
        .flatMap(response -> response.get(new BestBetsDataExtractionStrategy()))
        .orElse(Collections.emptyList());
  }

  private String getPreparePublishBestBetsAction(int projectId) {
    return commonPath + String.format(publishBestBetsForProjectAction, projectId);
  }


  private <T> HttpEntity createJsonPayloadEntity(T jsonPayload) {
    return EntityBuilder.create()
        .setText(new Gson().toJson(jsonPayload))
        .setContentType(ContentType.APPLICATION_JSON)
        .setContentEncoding(StandardCharsets.UTF_8.name())
        .build();
  }

  private void validateCommonConfigs() {
    if (searchRequestExecutorService == null) {
      throw new IllegalStateException("Missing an instance of SearchRequestExecutorService.");
    }
    if (StringUtils.isBlank(commonPath)) {
      throw new IllegalStateException("Best bets api common path is not specified.");
    }
  }

  private void handleSearchResponseError(SearchResponse searchResponse, String exceptionMessage) {
    if (!searchResponse.isSuccess()) {
      searchResponse.get(new JsonObjectDataExtractionStrategy())
          .map(jsonObject -> new Gson().toJson(jsonObject))
          .ifPresent(s -> {
            throw new BestBetsActionFailedException(String.format("%s. Reason: %s", exceptionMessage, s));
          });
    }
  }

  private void handleFailedRequestExecution(Optional<SearchResponse> searchResponse) {
    if (!searchResponse.isPresent()) {
      throw new BestBetsActionFailedException(FAILED_REQUEST_EXECUTION);
    }
  }

}
