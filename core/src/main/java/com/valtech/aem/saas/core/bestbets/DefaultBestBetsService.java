package com.valtech.aem.saas.core.bestbets;

import com.google.gson.Gson;
import com.valtech.aem.saas.api.bestbets.BestBetsActionFailedException;
import com.valtech.aem.saas.api.bestbets.BestBetsService;
import com.valtech.aem.saas.api.bestbets.dto.BestBetDTO;
import com.valtech.aem.saas.api.bestbets.dto.BestBetPayloadDTO;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.core.bestbets.DefaultBestBetsService.Configuration;
import com.valtech.aem.saas.core.http.client.SearchAdminRequestExecutorService;
import com.valtech.aem.saas.core.http.client.SearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.request.*;
import com.valtech.aem.saas.core.http.response.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component(service = BestBetsService.class)
@ServiceDescription("Search as a Service - Best Bets Service")
@Designate(ocd = Configuration.class)
public class DefaultBestBetsService implements BestBetsService {

    public static final String URL_PATH_DELIMITER = "/";
    public static final String STRING_FORMAT_PLACEHOLDER = "%s";
    public static final String FAILED_REQUEST_EXECUTION = "Failed to execute request to best bets api.";

    @Reference
    private SearchAdminRequestExecutorService searchAdminRequestExecutorService;

    @Reference
    private SearchServiceConnectionConfigurationService searchServiceConnectionConfigurationService;

    private Configuration configuration;

    @Override
    public int addBestBet(@NonNull SearchCAConfigurationModel searchConfiguration,
                          @NonNull BestBetPayloadDTO bestBetPayloadDto) {
        if (StringUtils.isBlank(configuration.bestBetsService_apiAddBestBetAction())) {
            throw new IllegalStateException("Add best bet action path is not specified.");
        }

        SearchRequest searchRequest = SearchRequestPost.builder()
                                                       .uri(createApiCommonPath() + configuration.bestBetsService_apiAddBestBetAction())
                                                       .httpEntity(createJsonPayloadEntity(bestBetPayloadDto.index(
                                                               searchConfiguration.getIndex())))
                                                       .build();
        Optional<SearchResponse> searchResponse = searchAdminRequestExecutorService.execute(searchRequest);
        handleFailedRequestExecution(searchResponse);
        searchResponse.ifPresent(r -> handleSearchResponseError(r,
                                                                String.format("Failed to add best bet: %s",
                                                                              bestBetPayloadDto)));
        return searchResponse.flatMap(response -> response.get(new BestBetIdDataExtractionStrategy()))
                             .orElseThrow(() -> new BestBetsActionFailedException(
                                     "Failed to extract best bet id from response."));
    }

    @Override
    public void addBestBets(@NonNull SearchCAConfigurationModel searchConfiguration,
                            @NonNull List<BestBetPayloadDTO> bestBetPayloadList) {
        if (StringUtils.isBlank(configuration.bestBetsService_apiAddBestBetsAction())) {
            throw new IllegalStateException("Add best bets action path is not specified.");
        }
        SearchRequest searchRequest = SearchRequestPost.builder()
                                                       .uri(createApiCommonPath() + configuration.bestBetsService_apiAddBestBetsAction())
                                                       .httpEntity(createJsonPayloadEntity(bestBetPayloadList.stream()
                                                                                                             .map(bestBetPayloadDTO -> bestBetPayloadDTO.index(
                                                                                                                     searchConfiguration.getIndex()))
                                                                                                             .collect(
                                                                                                                     Collectors.toList())))
                                                       .build();
        Optional<SearchResponse> searchResponse = searchAdminRequestExecutorService.execute(searchRequest);
        handleFailedRequestExecution(searchResponse);
        searchResponse.ifPresent(r -> handleSearchResponseError(r,
                                                                String.format("Failed to add best bets: %s",
                                                                              bestBetPayloadList)));
    }

    @Override
    public void updateBestBet(@NonNull SearchCAConfigurationModel searchConfiguration,
                              int bestBetId,
                              @NonNull BestBetPayloadDTO bestBetPayloadDto) {
        if (StringUtils.isBlank(configuration.bestBetsService_apiUpdateBestBetAction())) {
            throw new IllegalStateException("Update best bet action path is not specified.");
        }
        SearchRequest searchRequest = SearchRequestPut.builder()
                                                      .uri(createApiCommonPath() + configuration.bestBetsService_apiUpdateBestBetAction() + URL_PATH_DELIMITER + bestBetId)
                                                      .httpEntity(createJsonPayloadEntity(bestBetPayloadDto.index(
                                                              searchConfiguration.getIndex())))
                                                      .build();
        Optional<SearchResponse> searchResponse = searchAdminRequestExecutorService.execute(searchRequest);
        handleFailedRequestExecution(searchResponse);
        searchResponse.ifPresent(r -> handleSearchResponseError(r,
                                                                String.format(
                                                                        "Failed to update best bet with id %s, with %s",
                                                                        bestBetId,
                                                                        bestBetPayloadDto)));
        if (!searchResponse.flatMap(response -> response.get(new ModifiedBestBetIdExtractionStrategy())).isPresent()) {
            throw new BestBetsActionFailedException(String.format(
                    "Failed to update best bet: %s with the following update details %s",
                    bestBetId,
                    bestBetPayloadDto));
        }
    }

    @Override
    public void deleteBestBet(@NonNull SearchCAConfigurationModel searchConfiguration, int bestBetId) {
        if (StringUtils.isBlank(configuration.bestBetsService_apiDeleteBestBetAction())) {
            throw new IllegalStateException("Delete best bet action path is not specified.");
        }
        SearchRequest searchRequest = SearchRequestDelete.builder()
                                                         .uri(createApiCommonPath() + configuration.bestBetsService_apiDeleteBestBetAction() + URL_PATH_DELIMITER + bestBetId)
                                                         .build();
        Optional<SearchResponse> searchResponse = searchAdminRequestExecutorService.execute(searchRequest);
        handleFailedRequestExecution(searchResponse);
        searchResponse.ifPresent(r -> handleSearchResponseError(r,
                                                                String.format("Failed to delete best bet with id %s",
                                                                              bestBetId)));
        if (!searchResponse.filter(SearchResponse::isSuccess)
                           .flatMap(response -> response.get(new ModifiedBestBetIdExtractionStrategy()))
                           .isPresent()) {
            throw new BestBetsActionFailedException(String.format("Failed to delete best bet: %s", bestBetId));
        }
    }

    @Override
    public void publishBestBetsForProject(@NonNull SearchCAConfigurationModel searchConfiguration, int projectId) {
        if (StringUtils.isBlank(configuration.bestBetsService_apiPublishProjectBestBetsAction())) {
            throw new IllegalStateException("Publish best bets for project action path is not specified.");
        }
        if (!StringUtils.contains(configuration.bestBetsService_apiPublishProjectBestBetsAction(),
                                  STRING_FORMAT_PLACEHOLDER)) {
            throw new IllegalArgumentException(
                    "Publish Best Bets For Project Action is of illegal format. It should contain a " +
                            "wildcard/placeholder for project id.");
        }
        SearchRequest searchRequest = new SearchRequestGet(getPreparePublishBestBetsAction(projectId));
        Optional<SearchResponse> searchResponse = searchAdminRequestExecutorService.execute(searchRequest);
        handleFailedRequestExecution(searchResponse);
        searchResponse.ifPresent(r -> handleSearchResponseError(r,
                                                                String.format(
                                                                        "Failed to publish best bets for project: %s",
                                                                        projectId)));
    }

    @Override
    public List<BestBetDTO> getBestBets(@NonNull SearchCAConfigurationModel searchConfiguration) {
        if (StringUtils.isBlank(configuration.bestBetsService_apiGetAllBestBetsAction())) {
            throw new IllegalStateException("Get best bets action path is not specified.");
        }
        SearchRequest searchRequest =
                new SearchRequestGet(createApiCommonPath() + configuration.bestBetsService_apiGetAllBestBetsAction());
        Optional<SearchResponse> searchResponse = searchAdminRequestExecutorService.execute(searchRequest);
        handleFailedRequestExecution(searchResponse);
        return searchAdminRequestExecutorService.execute(searchRequest)
                                                .filter(SearchResponse::isSuccess)
                                                .flatMap(response -> response.get(new BestBetsDataExtractionStrategy()))
                                                .orElse(Collections.emptyList());
    }

    private String getPreparePublishBestBetsAction(int projectId) {
        return createApiCommonPath() + String.format(configuration.bestBetsService_apiPublishProjectBestBetsAction(),
                                                     projectId);
    }


    private <T> HttpEntity createJsonPayloadEntity(T jsonPayload) {
        return EntityBuilder.create()
                            .setText(new Gson().toJson(jsonPayload))
                            .setContentType(ContentType.APPLICATION_JSON)
                            .setContentEncoding(StandardCharsets.UTF_8.name())
                            .build();
    }

    private void handleSearchResponseError(SearchResponse searchResponse, String exceptionMessage) {
        if (!searchResponse.isSuccess()) {
            searchResponse.get(new JsonObjectDataExtractionStrategy())
                          .map(jsonObject -> new Gson().toJson(jsonObject))
                          .ifPresent(s -> {
                              throw new BestBetsActionFailedException(String.format("%s. Reason: %s",
                                                                                    exceptionMessage,
                                                                                    s));
                          });
        }
    }

    private void handleFailedRequestExecution(Optional<SearchResponse> searchResponse) {
        if (!searchResponse.isPresent()) {
            throw new BestBetsActionFailedException(FAILED_REQUEST_EXECUTION);
        }
    }

    private String createApiCommonPath() {
        return StringUtils.join(searchAdminRequestExecutorService.getBaseUrl(),
                                configuration.bestBetsService_apiVersionPath());
    }

    @Activate
    @Modified
    private void activate(Configuration configuration) {
        this.configuration = configuration;
    }


    @ObjectClassDefinition(name = "Search as a Service - Best Bets Service Configuration",
                           description = "Best Bets Api specific details.")
    public @interface Configuration {

        String DEFAULT_API_BEST_BET_ACTION = "/bestbet";
        String DEFAULT_API_BEST_BETS_ACTION = "/bestbets";
        String DEFAULT_API_PROJECT_BEST_BETS_PUBLISH_ACTION = DEFAULT_API_BEST_BETS_ACTION + "/%s/publish";
        String DEFAULT_API_VERSION_PATH = "/api/v3"; // NOSONAR

        @AttributeDefinition(name = "Api version path",
                             description = "Path designating the api version.",
                             type = AttributeType.STRING) String bestBetsService_apiVersionPath() default DEFAULT_API_VERSION_PATH; // NOSONAR

        @AttributeDefinition(name = "Api best bet add action",
                             description = "Path designating the action of adding a best bet entry.",
                             type = AttributeType.STRING) String bestBetsService_apiAddBestBetAction() default DEFAULT_API_BEST_BET_ACTION; // NOSONAR

        @AttributeDefinition(name = "Api best bets add action",
                             description = "Path designating the action of adding a list of best bets.",
                             type = AttributeType.STRING) String bestBetsService_apiAddBestBetsAction() default DEFAULT_API_BEST_BETS_ACTION; // NOSONAR

        @AttributeDefinition(name = "Api best bets update action",
                             description = "Path designating the action of updating a best bet entry.",
                             type = AttributeType.STRING) String bestBetsService_apiUpdateBestBetAction() default DEFAULT_API_BEST_BETS_ACTION; // NOSONAR

        @AttributeDefinition(name = "Api best bet delete action",
                             description = "Path designating the action of deleting a best bet entry.",
                             type = AttributeType.STRING) String bestBetsService_apiDeleteBestBetAction() default DEFAULT_API_BEST_BETS_ACTION; // NOSONAR

        @AttributeDefinition(name = "Api project's best bets publish action",
                             description = "Path designating the action of publishing the best bets for a project.",
                             type = AttributeType.STRING) String bestBetsService_apiPublishProjectBestBetsAction() default DEFAULT_API_PROJECT_BEST_BETS_PUBLISH_ACTION; // NOSONAR

        @AttributeDefinition(name = "Api best bets get action",
                             description = "Path designating the action of getting all best bets.",
                             type = AttributeType.STRING) String bestBetsService_apiGetAllBestBetsAction() default DEFAULT_API_BEST_BETS_ACTION; // NOSONAR
    }
}
