package com.valtech.aem.saas.core.bestbets;

import com.valtech.aem.saas.api.bestbets.BestBetsService;
import com.valtech.aem.saas.api.bestbets.BestBetsConsumerService;
import com.valtech.aem.saas.core.bestbets.DefaultBestBetsService.Configuration;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.client.SearchServiceConnectionConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Slf4j
@Component(name = "Search as a Service - Best Bets Service",
    service = BestBetsService.class)
@Designate(ocd = Configuration.class)
public class DefaultBestBetsService implements BestBetsService {

  public static final String URL_PATH_DELIMITER = "/";

  @Reference
  private SearchRequestExecutorService searchRequestExecutorService;

  @Reference
  private SearchServiceConnectionConfigurationService searchServiceConnectionConfigurationService;

  private Configuration configuration;

  @Override
  public BestBetsConsumerService getBestBetsConsumerService(String client) {
    if (StringUtils.isBlank(client)) {
      throw new IllegalArgumentException("Must specify client id.");
    }
    return ClientBestBetsConsumerService.builder()
        .searchRequestExecutorService(searchRequestExecutorService)
        .commonPath(createApiCommonPath(client))
        .addBestBetAction(configuration.bestBetsService_apiAddBestBetAction())
        .addBestBetsAction(configuration.bestBetsService_apiAddBestBetsAction())
        .updateBestBetAction(configuration.bestBetsService_apiUpdateBestBetAction())
        .deleteBestBetAction(configuration.bestBetsService_apiDeleteBestBetAction())
        .getBestBetsAction(configuration.bestBetsService_apiGetAllBestBetsAction())
        .publishBestBetsForProjectAction(configuration.bestBetsService_apiPublishProjectBestBetsAction())
        .build();
  }

  private String createApiCommonPath(String client) {
    return StringUtils.join(
        searchServiceConnectionConfigurationService.getBaseUrl(),
        configuration.bestBetsService_apiBasePath(),
        URL_PATH_DELIMITER + client,
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
    String DEFAULT_API_BASE_PATH = "/admin";
    String DEFAULT_API_VERSION_PATH = "/api/v3";

    @AttributeDefinition(name = "Api base path",
        description = "Base path of the api.",
        type = AttributeType.STRING)
    String bestBetsService_apiBasePath() default DEFAULT_API_BASE_PATH;

    @AttributeDefinition(name = "Api version path",
        description = "Path designating the api version.",
        type = AttributeType.STRING)
    String bestBetsService_apiVersionPath() default DEFAULT_API_VERSION_PATH;

    @AttributeDefinition(name = "Api best bet add action",
        description = "Path designating the action of adding a best bet entry.",
        type = AttributeType.STRING)
    String bestBetsService_apiAddBestBetAction() default DEFAULT_API_BEST_BET_ACTION;

    @AttributeDefinition(name = "Api best bets add action",
        description = "Path designating the action of adding a list of best bets.",
        type = AttributeType.STRING)
    String bestBetsService_apiAddBestBetsAction() default DEFAULT_API_BEST_BETS_ACTION;

    @AttributeDefinition(name = "Api best bets update action",
        description = "Path designating the action of updating a best bet entry.",
        type = AttributeType.STRING)
    String bestBetsService_apiUpdateBestBetAction() default DEFAULT_API_BEST_BETS_ACTION;

    @AttributeDefinition(name = "Api best bet delete action",
        description = "Path designating the action of deleting a best bet entry.",
        type = AttributeType.STRING)
    String bestBetsService_apiDeleteBestBetAction() default DEFAULT_API_BEST_BETS_ACTION;

    @AttributeDefinition(name = "Api project's best bets publish action",
        description = "Path designating the action of publishing the best bets for a project.",
        type = AttributeType.STRING)
    String bestBetsService_apiPublishProjectBestBetsAction() default DEFAULT_API_PROJECT_BEST_BETS_PUBLISH_ACTION;

    @AttributeDefinition(name = "Api best bets get action",
        description = "Path designating the action of getting all best bets.",
        type = AttributeType.STRING)
    String bestBetsService_apiGetAllBestBetsAction() default DEFAULT_API_BEST_BETS_ACTION;
  }
}
