package com.valtech.aem.saas.api.bestbets;

import com.valtech.aem.saas.api.bestbets.dto.BestBetDTO;
import com.valtech.aem.saas.api.bestbets.dto.BestBetPayloadDTO;
import java.util.List;
import lombok.NonNull;
import org.apache.sling.api.resource.Resource;

/**
 * Represents a service that manages best bets.
 */
public interface BestBetsService {

  /**
   * Adds a best bet entry in saas admin.
   *
   * @param context        resource that specifies the context. used for resolving the client and index parameters.
   * @param bestBetPayload object containing details for the best bet.
   * @throws IllegalArgumentException      exception thrown when blank client argument is passed
   * @throws IllegalStateException         exception thrown when according action is not specified
   * @throws BestBetsActionFailedException exception thrown when the add action has failed or request execution has
   *                                       failed
   */
  void addBestBet(@NonNull Resource context, @NonNull BestBetPayloadDTO bestBetPayload);


  /**
   * Adds a list of best bet entry in saas admin.
   *
   * @param context            resource that specifies the context. used for resolving the client and index parameters.
   * @param bestBetPayloadList a list of objects containing best bet details.
   * @throws IllegalArgumentException      exception thrown when blank client argument is passed
   * @throws IllegalStateException         exception thrown when according action is not specified
   * @throws BestBetsActionFailedException exception thrown when the add action has failed or request execution has
   *                                       failed
   */
  void addBestBets(@NonNull Resource context, @NonNull List<BestBetPayloadDTO> bestBetPayloadList);


  /**
   * Updates the best bet entry with the specified id.
   *
   * @param context        resource that specifies the context. used for resolving the client and index parameters.
   * @param bestBetId      id of the best bet that is updated.
   * @param bestBetPayload best bet details to be updated.
   * @throws IllegalArgumentException      exception thrown when blank client argument is passed
   * @throws IllegalStateException         exception thrown when according action is not specified
   * @throws BestBetsActionFailedException exception thrown when the update action has failed or request execution has
   *                                       failed
   */
  void updateBestBet(@NonNull Resource context, int bestBetId, @NonNull BestBetPayloadDTO bestBetPayload);

  /**
   * Deletes the best bet entry with the specified id.
   *
   * @param context   resource that specifies the context. used for resolving the client and index parameters.
   * @param bestBetId the id of the best bet that should be deleted.
   * @throws IllegalArgumentException      exception thrown when blank client argument is passed
   * @throws IllegalStateException         exception thrown when according action is not specified
   * @throws BestBetsActionFailedException exception thrown when the delete action has failed or request execution has
   *                                       failed.
   */
  void deleteBestBet(@NonNull Resource context, int bestBetId);

  /**
   * Published the best bets for the specified project.
   *
   * @param context   resource that specifies the context. used for resolving the client and index parameters.
   * @param projectId id of the project whose best bets should be published
   * @throws IllegalArgumentException      exception thrown when blank client argument is passed
   * @throws IllegalStateException         exception thrown when according action is not specified
   * @throws BestBetsActionFailedException exception thrown when the publishing action has failed or request execution
   *                                       has failed
   */
  void publishBestBetsForProject(@NonNull Resource context, int projectId);

  /**
   * Gets the list of all best bets entries.
   *
   * @param context resource that specifies the context. used for resolving the client and index parameters.
   * @return list of best bets.
   * @throws IllegalArgumentException      exception thrown when blank client argument is passed
   * @throws IllegalStateException         exception thrown when according action is not specified
   * @throws BestBetsActionFailedException exception thrown when the request execution has failed.
   */
  List<BestBetDTO> getBestBets(@NonNull Resource context);

}
