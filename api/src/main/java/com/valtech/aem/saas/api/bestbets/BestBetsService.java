package com.valtech.aem.saas.api.bestbets;

import com.valtech.aem.saas.api.bestbets.dto.BestBetDTO;
import com.valtech.aem.saas.api.bestbets.dto.BestBetPayloadDTO;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import lombok.NonNull;

import java.util.List;

/**
 * Service that manages best bets.
 */
public interface BestBetsService {

    /**
     * Adds a best bet entry in saas admin.
     *
     * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
     * @param bestBetPayload      object containing details for the best bet.
     * @return best bet id
     * @throws IllegalArgumentException      exception thrown when blank client argument is passed
     * @throws IllegalStateException         exception thrown when according action is not specified
     * @throws BestBetsActionFailedException exception thrown when the add action has failed or request execution has
     *                                       failed
     */
    int addBestBet(
            @NonNull SearchCAConfigurationModel searchConfiguration,
            @NonNull BestBetPayloadDTO bestBetPayload);


    /**
     * Adds a list of best bet entry in saas admin.
     *
     * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
     * @param bestBetPayloadList  a list of objects containing best bet details.
     * @throws IllegalArgumentException      exception thrown when blank client argument is passed
     * @throws IllegalStateException         exception thrown when according action is not specified
     * @throws BestBetsActionFailedException exception thrown when the add action has failed or request execution has
     *                                       failed
     */
    void addBestBets(
            @NonNull SearchCAConfigurationModel searchConfiguration,
            @NonNull List<BestBetPayloadDTO> bestBetPayloadList);


    /**
     * Updates the best bet entry with the specified id.
     *
     * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
     * @param bestBetId           id of the best bet that is updated.
     * @param bestBetPayload      best bet details to be updated.
     * @throws IllegalArgumentException      exception thrown when blank client argument is passed
     * @throws IllegalStateException         exception thrown when according action is not specified
     * @throws BestBetsActionFailedException exception thrown when the update action has failed or request execution has
     *                                       failed
     */
    void updateBestBet(
            @NonNull SearchCAConfigurationModel searchConfiguration,
            int bestBetId,
            @NonNull BestBetPayloadDTO bestBetPayload);

    /**
     * Deletes the best bet entry with the specified id.
     *
     * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
     * @param bestBetId           the id of the best bet that should be deleted.
     * @throws IllegalArgumentException      exception thrown when blank client argument is passed
     * @throws IllegalStateException         exception thrown when according action is not specified
     * @throws BestBetsActionFailedException exception thrown when the delete action has failed or request execution has
     *                                       failed.
     */
    void deleteBestBet(
            @NonNull SearchCAConfigurationModel searchConfiguration,
            int bestBetId);

    /**
     * Published the best bets for the specified project.
     *
     * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
     * @param projectId           id of the project whose best bets should be published
     * @throws IllegalArgumentException      exception thrown when blank client argument is passed
     * @throws IllegalStateException         exception thrown when according action is not specified
     * @throws BestBetsActionFailedException exception thrown when the publishing action has failed or request execution
     *                                       has failed
     */
    void publishBestBetsForProject(
            @NonNull SearchCAConfigurationModel searchConfiguration,
            int projectId);

    /**
     * Gets the list of all best bets entries.
     *
     * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
     * @return list of best bets.
     * @throws IllegalArgumentException      exception thrown when blank client argument is passed
     * @throws IllegalStateException         exception thrown when according action is not specified
     * @throws BestBetsActionFailedException exception thrown when the request execution has failed.
     */
    List<BestBetDTO> getBestBets(@NonNull SearchCAConfigurationModel searchConfiguration);

}
