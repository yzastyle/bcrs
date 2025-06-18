package com.quest.bank_card.service;

import com.quest.bank_card.entity.Money;
import com.quest.bank_card.exception.InsufficientFundsException;
import com.quest.bank_card.exception.UnauthorizedException;
import com.quest.bank_card.exception.ValidationException;

import java.util.UUID;

/**
 * Service for secure money transfers between cards owned by the same user.
 *
 *
 * <p><strong>Thread safety:</strong>
 * All transfer operations use pessimistic locking to ensure atomicity
 * and prevent concurrent modification issues during balance updates.
 *
 * @see CardManagementService
 * @see Money
 * @author Devon
 */
public interface TransferService {

    /**
     * Executes an atomic money transfer between two cards owned by the same user.
     * <p>
     * Performs comprehensive validation including ownership verification,
     * card status checks, and sufficient funds validation. Uses pessimistic
     * locking on both cards to ensure consistency in concurrent scenarios.
     *
     * <p><strong>Validation sequence:</strong>
     * <ol>
     *   <li>Verifies different card IDs (prevents self-transfer)</li>
     *   <li>Confirms both cards belong to the specified user</li>
     *   <li>Validates both cards have ACTIVE status</li>
     *   <li>Checks sufficient funds in source card</li>
     * </ol>
     *
     * <p><strong>Concurrency handling:</strong>
     * Both cards are locked using {@code findCardByIdWithLock} to prevent
     * concurrent balance modifications during the transfer operation.
     *
     * @param fromCardId the source card identifier, must be owned by userId
     * @param toCardId the destination card identifier, must be owned by userId
     * @param userId the owner of both cards
     * @param amount the transfer amount, must be positive and available in source card
     * @throws ValidationException if fromCardId equals toCardId
     * @throws UnauthorizedException if either card is not owned by userId
     * @throws InsufficientFundsException if source card has insufficient funds
     */
    void transferBetweenUserCards(UUID fromCardId, UUID toCardId, UUID userId, Money amount);
}
