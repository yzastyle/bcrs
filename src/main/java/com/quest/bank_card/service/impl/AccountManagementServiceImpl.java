package com.quest.bank_card.service.impl;

import com.quest.bank_card.dto.CardSearchCriteriaDto;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.entity.User;
import com.quest.bank_card.exception.UnauthorizedException;
import com.quest.bank_card.exception.ValidationException;
import com.quest.bank_card.repository.specification.CardSpecifications;
import com.quest.bank_card.service.AccountManagementService;
import com.quest.bank_card.service.CardManagementService;
import com.quest.bank_card.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountManagementServiceImpl implements AccountManagementService {
    private final UserManagementService userManagementService;
    private final CardManagementService cardManagementService;

    @Transactional
    @Override
    public Card saveCard(UUID id, Card card) {
        User user = userManagementService.findUserById(id);
        card.setUser(user);
        return cardManagementService.saveCard(card);
    }

    @Transactional
    @Override
    public Card updateCard(UUID userId, UUID cardId, String newOwner) {
        validateOwner(cardId, userId);
        Card card = cardManagementService.findCardById(cardId);
        card.updateOwner(newOwner);
        return cardManagementService.saveCard(card);
    }

    @Transactional
    @Override
    public List<Card> saveCards(UUID id, List<Card> cards) {
        User user = userManagementService.findUserById(id);
        for (Card card: cards) {
            card.setUser(user);
        }
        return cardManagementService.saveAllCards(cards);
    }

    @Transactional
    @Override
    public void deleteCardById(UUID userId, UUID cardId) {
        validateOwner(cardId, userId);
        Card card = cardManagementService.findCardById(cardId);
        validateBalance(card);
        cardManagementService.deleteCardById(cardId);
    }

    @Transactional
    @Override
    public void deleteCardsByIds(UUID userId, List<UUID> cardsIds) {
        List<Card> cards = cardManagementService.findAllByIds(cardsIds);
        for (Card card : cards) {
            validateOwner(card.getId(), userId);
            validateBalance(card);
        }
        cardManagementService.deleteCardsByIds(cardsIds);
    }

    @Transactional
    @Override
    public void deleteCardsByUserId(UUID userId) {
        List<Card> cards = cardManagementService.findCardByUserId(userId);
        for (Card card : cards) {
            validateBalance(card);
        }
        cardManagementService.deleteCardsByIds(cards.stream().map(Card::getId).toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> findCardsByUserId(UUID userId) {
        return cardManagementService.findCardByUserId(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Card> findCardsByCriteria(UUID userId, CardSearchCriteriaDto cardSearchCriteriaDto,
                                                           Pageable pageable) {
        Specification<Card> spec = CardSpecifications.withCriteria(userId, cardSearchCriteriaDto);
        return cardManagementService.findCardsByCriteria(spec, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Card findUserCard(UUID userId, UUID cardId) {
        validateOwner(cardId, userId);
        return cardManagementService.findCardById(cardId);
    }

    @Override
    @Transactional(readOnly = true)
    public Money getDepositByCardId(UUID userId, UUID cardId) {
        validateOwner(cardId, userId);
        return cardManagementService.findCardById(cardId).getDeposit();
    }

    @Override
    @Transactional
    public void blockRequest(UUID userId, UUID cardId) {
        validateOwner(cardId, userId);
        cardManagementService.updateCardStatusById(cardId, "BLOCKED");
    }

    private void validateOwner(UUID cardId, UUID userId) {
        if (!cardManagementService.isCardOwnedBy(cardId, userId)) {
            throw new UnauthorizedException("Card does not belong to user. Operation cancelled.");
        }
    }

    private void validateBalance(Card card) {
        if (card.getDeposit().isGreaterThan(new Money(BigDecimal.ZERO))) {
            throw new ValidationException("Cannot delete card id=" + card.getId() + " with non-zero balance");
        }
    }
}
