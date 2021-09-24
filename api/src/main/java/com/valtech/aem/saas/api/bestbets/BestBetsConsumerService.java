package com.valtech.aem.saas.api.bestbets;

import java.util.List;
import lombok.NonNull;

/**
 * Represents the best bets consumer service. Offers the following actions: - add a single best bet - add multiple best
 * bets - update the best bet - delete a best bet - publish best bets for project - get all best bets
 */
public interface BestBetsConsumerService {

  /**
   * Adds a best bet entry in saas admin.
   *
   * @param bestBetPayload object containing details for the best bet.
   * @throws BestBetsActionFailedException exception thrown when the add action has failed or request execution has
   *                                       failed
   */
  void addBestBet(@NonNull BestBetPayload bestBetPayload);

  /**
   * Adds a list of best bet entry in saas admin.
   *
   * @param bestBetPayloadList a list of objects containing best bet details.
   * @throws BestBetsActionFailedException exception thrown when the add action has failed
   * or request execution has failed
   */
  void addBestBets(@NonNull List<BestBetPayload> bestBetPayloadList);

  /**
   * Updates the best bet entry with the specified betId.
   *
   * @param betId          id of the best bet that is updated.
   * @param bestBetPayload best bet details to be updated.
   * @return the id of the best bet entry that is updated.
   * @throws BestBetsActionFailedException exception thrown when the update action has failed
   * or request execution has failed
   */
  int updateBestBet(int betId, @NonNull BestBetPayload bestBetPayload);

  /**
   * Deletes the best bet entry with the specified betId.
   *
   * @param betId the id of the best bet that should be deleted.
   * @return the id of the best bet that is deleted
   * @throws BestBetsActionFailedException exception thrown when the delete action has failed
   * or request execution has failed.
   */
  int deleteBestBet(int betId);

  /**
   * Published the best bets for the specified project.
   *
   * @param projectId id of the project whose best bets should be published
   * @throws BestBetsActionFailedException exception thrown when the publishing action has failed
   * or request execution has failed
   */
  void publishBestBetsForProject(int projectId);

  /**
   * Gets the list of all best bets entries.
   *
   * @return list of best bets.
   * @throws BestBetsActionFailedException exception thrown when the request execution has failed.
   */
  List<BestBet> getBestBets();
}
