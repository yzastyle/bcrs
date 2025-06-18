package com.quest.bank_card.service;

import com.quest.bank_card.dto.CardCreateDto;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.exception.BaseException;
import com.quest.bank_card.repository.CardRepository;

/**
 * Service for generating new bank cards with secure, unique identifiers.
 * <p>
 * Provides functionality for creating bank cards with auto-generated card numbers,
 * expiration dates, and proper formatting of cardholder information.
 *
 * @see Card
 * @see CardCreateDto
 * @see CardRepository
 * @author Devon
 */
public interface CardGenerationService {
    /**
     * Generates a new bank card with unique identifier and system-defined properties.
     * <p>
     * Creates a complete bank card entity with auto-generated 16-digit card number,
     * 4-year expiration date, and normalized cardholder name. The card is initialized
     * with ACTIVE status and zero balance, ready for immediate use.
     *
     * <p><strong>Data formats:</strong>
     * <ul>
     *   <li>Card number: 16-digit string generated using {@code SecureRandom}</li>
     *   <li>Expiration date: MM/yy format, exactly 4 years from generation</li>
     *   <li>Owner name: Normalized to uppercase Latin via {@code CardUtil.toUpperLatinOwnerName()}</li>
     * </ul>
     *
     * <p><strong>Uniqueness handling:</strong>
     * Attempts up to 100 database checks to ensure card number uniqueness.
     * Each generation involves a database lookup for collision detection.
     *
     * @param cardCreateDto the card creation request containing cardholder information,
     *                     must not be {@code null} and must contain valid owner name
     * @return a new {@code Card} entity with unique number, normalized name, future expiration,
     *         ACTIVE status, and zero initial balance
     * @throws BaseException if unable to generate a unique card number after 100 attempts
     */
    Card generateCard(CardCreateDto cardCreateDto);

}
