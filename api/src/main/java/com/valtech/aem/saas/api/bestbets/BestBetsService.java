package com.valtech.aem.saas.api.bestbets;

/**
 * Represents a service that manages best bets.
 */
public interface BestBetsService {

  /**
   * Returns the best bets consumer object that provides actions for consuming/managing best bets, for a specified
   * client identifier.
   *
   * @param client identifying string.
   * @return best bets consumer object
   * @throws IllegalArgumentException exception thrown when blank client argument is passed
   */
  BestBetsConsumerService getBestBetsConsumerService(String client);
}
