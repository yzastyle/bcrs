package com.quest.bank_card.service;

import com.quest.bank_card.dto.CardSearchCriteriaDto;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.entity.User;
import com.quest.bank_card.exception.CardNotFoundException;
import com.quest.bank_card.exception.UnauthorizedException;
import com.quest.bank_card.exception.UserNotFoundException;
import com.quest.bank_card.exception.ValidationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing user accounts.
 * <p>
 * Implements business workflows for handling user accounts.
 * Centralizes CRUD operations for users' cards with mandatory verification
 * of card ownership. Unlike CardManagementService, all operations are executed
 * in the context of a specific user and require access rights validation.
 *
 * <p><strong>Key features:</strong>
 * <ul>
 *   <li>Cards can only be deleted when balance = 0 (prevents loss of funds)</li>
 *   <li>Support for bulk operations (creating/deleting multiple cards)</li>
 *   <li>Filtering cards by status, creation date, and expiration date</li>
 *   <li>User-initiated card blocking</li>
 * </ul>
 *
 *
 * @see CardManagementService
 * @see UserManagementService
 * @see Card
 * @see Money
 * @see User
 * @author Devon
 */
public interface AccountManagementService {

    /**
     * Creates and saves a new card for the specified user.
     * <p>
     * Validates user ownership and card data before persisting.
     * The card will be assigned to the user identified by the provided ID
     * and initialized with default settings (random number, active status, zero deposit).
     *
     * @param id the unique identifier of the card owner, must not be {@code null}
     * @param card the card entity to create, must not be {@code null}
     * @return the saved card with generated system fields (create date timestamp, ID)
     * @throws UserNotFoundException if user with specified ID does not exist
     */
    Card saveCard(UUID userId, Card card);

    /**
     * Updates the owner name of an existing card.
     * <p>
     * Allows users to update their name on their cards without changing
     * other card properties. Validates that the requesting user owns the card.
     *
     * @param userId the ID of the user requesting the update, must not be {@code null}
     * @param cardId the ID of the card to update, must not be {@code null}
     * @param newOwner the new owner name, must not be {@code null} or blank
     * @return the updated card with new owner name
     * @throws UnauthorizedException if user does not own the card
     */
    Card updateCard(UUID userId, UUID cardId, String newOwner);

    /**
     * Creates and saves multiple cards for the specified user in a single transaction.
     * <p>
     * Bulk operation for creating multiple cards simultaneously.
     *
     *
     * @param id the unique identifier of the card owner, must not be {@code null}
     * @param cards the list of cards to create, must not be {@code null} or empty
     * @return the list of saved cards in the same order as input, with generated system fields
     * @throws UserNotFoundException if user with specified ID does not exist
     */
    List<Card> saveCards(UUID id, List<Card> cards);

    /**
     * Deletes a specific card belonging to the user.
     * <p>
     * Permanently removes the card from the system. The card can only be deleted if its balance is zero
     * to prevent accidental loss of funds and belong to the requesting user.
     *
     * <p><strong>Important:</strong>
     * This operation is irreversible. Consider using card deactivation
     * for temporary suspension instead of deletion.
     *
     * @param userId the ID of the user requesting deletion, must not be {@code null}
     * @param cardId the ID of the card to delete, must not be {@code null}
     * @throws CardNotFoundException if card does not exist
     * @throws ValidationException if card has non-zero balance
     * @throws UnauthorizedException if user does not own the card
     */
    void deleteCardById(UUID userId, UUID cardId);

    /**
     * Deletes multiple cards belonging to the user in a single transaction.
     * <p>
     * Bulk deletion operation. All cards must have zero balance and belong
     * to the requesting user.
     *
     * <p><strong>Atomicity guarantee:</strong>
     * Either all specified cards are deleted successfully, or none are deleted
     * if any validation or business rule check fails.
     *
     * @param userId the ID of the user requesting deletion, must not be {@code null}
     * @param cardsIds the list of card IDs to delete, must not be {@code null} or empty
     * @throws ValidationException if any card has non-zero balance
     * @throws UnauthorizedException if user does not own the card
     */
    void deleteCardsByIds(UUID userId, List<UUID> cardsIds);

    /**
     * Deletes all cards belonging to the specified user.
     * <p>
     * Administrative operation typically used during user account closure.
     * Only cards with zero balance can be deleted. Cards with positive balance
     * will cause the operation to fail with detailed information about
     * which cards prevent deletion.
     *
     * <p><strong>Warning:</strong>
     * This is a destructive operation that cannot be undone.
     * Ensure all funds are transferred before calling this method.
     *
     * @param userId the ID of the user whose cards should be deleted, must not be {@code null}
     * @throws ValidationException if any card has non-zero balance
     */
    void deleteCardsByUserId(UUID userId);

    /**
     * Retrieves all cards belonging to the specified user.
     * <p>
     * Returns a complete list of cards owned by the user.
     * The list includes active, blocked, and expired cards but excludes
     * previously deleted cards.
     *
     * <p><strong>Performance note:</strong>
     * For large numbers of cards, consider using
     * {@link #findCardsByCriteria(UUID, CardSearchCriteriaDto, Pageable)}
     * with pagination for better performance.
     *
     * @param userId the unique identifier of the card owner, must not be {@code null}
     * @return the list of cards owned by the user, empty list if user has no cards
     */
    List<Card> findCardsByUserId(UUID userId);

    /**
     * Searches for user's cards using specified criteria with pagination support.
     * <p>
     * Advanced search method that allows filtering by multiple criteria
     * including card status, creation date range.
     * Results are paginated for optimal performance.
     *
     *
     * @param userId the ID of the user whose cards to search, must not be {@code null}
     * @param cardSearchCriteriaDto the search criteria, may be {@code null} for no filtering
     * @param pageable the pagination information, must not be {@code null}
     * @return a paginated result containing matching cards and pagination metadata
     */
    Page<Card> findCardsByCriteria(UUID userId, CardSearchCriteriaDto cardSearchCriteriaDto,
                                                    Pageable pageable);

    /**
     * Retrieves a specific card belonging to the user.
     * <p>
     * Finds and returns a single card owned by the specified user.
     * Validates ownership before returning the card data to ensure
     * users can only access their own cards.
     *
     * @param userId the ID of the card owner, must not be {@code null}
     * @param cardId the ID of the card to retrieve, must not be {@code null}
     * @return the requested card if found and owned by the user
     * @throws CardNotFoundException if card does not exist
     * @throws UnauthorizedException if user does not own the card
     */
    Card findUserCard(UUID userId, UUID cardId);

    /**
     * Retrieves the current deposit (balance) information for a user's card.
     * <p>
     * Returns the current monetary balance associated with the specified card.
     * The balance includes all completed transactions and reflects the
     * real-time available amount on the card.
     *
     * <p><strong>Real-time guarantee:</strong>
     * The returned balance reflects all committed transactions
     * up to the moment of the query execution.
     *
     * @param userId the ID of the card owner, must not be {@code null}
     * @param cardId the ID of the card whose deposit to retrieve, must not be {@code null}
     * @return the current deposit information including amount and currency
     * @throws CardNotFoundException if card does not exist
     * @throws UnauthorizedException if user does not own the card
     */
    Money getDepositByCardId(UUID userId, UUID cardId);

    /**
     * Processes a user-initiated request to block their card.
     * <p>
     * Allows users to block their own cards for security purposes.
     * Once blocked, the card cannot be used for transactions until
     * administratively unblocked. This is typically used when a card
     * is lost, stolen, or compromised.
     *
     * @param userId the ID of the user requesting the block, must not be {@code null}
     * @param cardId the ID of the card to block, must not be {@code null}
     * @throws CardNotFoundException if card does not exist
     * @throws UnauthorizedException if user does not own the card
     * @throws ValidationException if card is already blocked or in invalid status
     */
    void blockRequest(UUID userId, UUID cardId);
}
