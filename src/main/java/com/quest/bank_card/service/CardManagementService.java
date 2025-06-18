package com.quest.bank_card.service;

import com.quest.bank_card.entity.Card;
import com.quest.bank_card.exception.CardNotFoundException;
import com.quest.bank_card.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Administrative service for low-level card management operations.
 * <p>
 * Provides direct data access and manipulation capabilities for card entities
 * without user context validation. Designed for administrative workflows,
 * system maintenance, and internal service integrations where ownership
 * verification is handled at higher application layers.
 *
 * @see AccountManagementService
 * @see Card
 * @see Specification
 * @author Devon
 */
public interface CardManagementService {

    Card saveCard(Card card);

    List<Card> saveAllCards(List<Card> cards);

    void deleteCardById(UUID id);

    void deleteCardsByIds(List<UUID> ids);

    List<Card> findAllCardsByIds(List<UUID> ids);

    Card findCardById(UUID id);

    /**
     * Retrieves a card with pessimistic database lock for exclusive access.
     * <p>
     * Uses {@code LockModeType.PESSIMISTIC_WRITE} to prevent concurrent modifications
     * during critical operations like status updates or balance transfers.
     * The lock is held until the current transaction commits or rolls back.
     *
     * @param id the unique identifier of the card, must not be {@code null}
     * @return the found card with exclusive lock
     * @throws CardNotFoundException if either card does not exist
     */
    Card findCardByIdWithLock(UUID id);

    /**
     * Retrieves all cards in the system with eager-loaded user relationships.
     *
     * @return the complete list of all cards with user data
     */
    List<Card> findAllCards();

    /**
     * Searches cards using dynamic JPA Specification with pagination support.
     * <p>
     * Enables complex filtering and sorting based on runtime-constructed queries.
     *
     * @param specification the dynamic query specification, may be {@code null} for no filtering
     * @param pageable the pagination and sorting parameters, must not be {@code null}
     * @return paginated results matching the specification
     */
    Page<Card> findCardsByCriteria(Specification<Card> specification, Pageable pageable);

    /**
     * Updates the status of a card by its identifier.
     * <p>
     * Loads the card entity, applies the status change via domain method,
     * and persists the update. Status validation is performed by the entity's
     * {@code updateStatus} method according to business rules.
     *
     * @param id the unique identifier of the card, must not be {@code null}
     * @param status the new status value, must not be {@code null}
     * @throws CardNotFoundException if no card exists with the specified ID
     * @throws ValidationException if status is invalid
     */
    void updateCardStatusById(UUID id, String status);

    boolean isCardOwnedBy(UUID cardId, UUID userId);

    List<Card> findCardsByUserId(UUID id);

    /**
     * Validates card expiration and automatically updates status if needed.
     * <p>
     * Uses {@code CardUtil.isExpired} for date validation logic.
     *
     * <p><strong>Idempotent operation:</strong>
     * Safe to call multiple times - only updates status if transition is valid.
     *
     * @param cardId the unique identifier of the card to validate, must not be {@code null}
     * @throws CardNotFoundException if no card exists with the specified ID
     * @throws ValidationException if status is invalid
     */
    void validateAndUpdateExpiredCard(UUID cardId);
}
