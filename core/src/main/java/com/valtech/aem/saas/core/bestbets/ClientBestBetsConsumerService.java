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
import com.valtech.aem.saas.core.http.response.ModifiedBestBetIdExtractionStrategy;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;

@Builder
public class ClientBestBetsConsumerService implements BestBetsConsumerService {

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

    if (!searchRequestExecutorService.execute(searchRequest)
        .filter(SearchResponse::isSuccess)
        .isPresent()) {
      throw new BestBetsActionFailedException(String.format("Failed to add best bet: %s", bestBetPayload));
    }
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

    if (!searchRequestExecutorService.execute(searchRequest)
        .filter(SearchResponse::isSuccess)
        .isPresent()) {
      throw new BestBetsActionFailedException(String.format("Failed to add best bets: %s", bestBetPayloadList));
    }
  }

  @Override
  public int updateBestBet(int betId, @NonNull BestBetPayload bestBetPayload) {
    validateCommonConfigs();
    if (StringUtils.isBlank(updateBestBetAction)) {
      throw new IllegalStateException("Update best bet action path is not specified.");
    }
    SearchRequest searchRequest = SearchRequestPut.builder()
        .uri(commonPath + updateBestBetAction + "/" + betId)
        .httpEntity(createJsonPayloadEntity(bestBetPayload))
        .build();

    return searchRequestExecutorService.execute(searchRequest)
        .filter(SearchResponse::isSuccess)
        .flatMap(response -> response.get(new ModifiedBestBetIdExtractionStrategy()))
        .orElseThrow(() -> new BestBetsActionFailedException(
            String.format("Failed to update best bet: %s with the following update details %s", betId,
                bestBetPayload)));
  }

  @Override
  public int deleteBestBet(int betId) {
    validateCommonConfigs();
    if (StringUtils.isBlank(deleteBestBetAction)) {
      throw new IllegalStateException("Delete best bet action path is not specified.");
    }
    SearchRequest searchRequest = SearchRequestDelete.builder()
        .uri(commonPath + deleteBestBetAction + "/" + betId)
        .build();

    return searchRequestExecutorService.execute(searchRequest)
        .filter(SearchResponse::isSuccess)
        .flatMap(response -> response.get(new ModifiedBestBetIdExtractionStrategy()))
        .orElseThrow(() -> new BestBetsActionFailedException(String.format("Failed to delete best bet: %s", betId)));
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
    if (!searchRequestExecutorService.execute(searchRequest)
        .filter(SearchResponse::isSuccess)
        .isPresent()) {
      throw new BestBetsActionFailedException(String.format("Failed to publish best bets for project: %s", projectId));
    }
  }

  @Override
  public List<BestBet> getBestBets() {
    validateCommonConfigs();
    if (StringUtils.isBlank(getBestBetsAction)) {
      throw new IllegalStateException("Get best bets action path is not specified.");
    }
    SearchRequest searchRequest = new SearchRequestGet(commonPath + getBestBetsAction);

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

}
