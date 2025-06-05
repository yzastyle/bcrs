package com.quest.bank_card.service;

import com.quest.bank_card.dto.CardSearchCriteriaDto;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface AccountManagementService {

    Card saveCard(UUID id, Card card);

    Card updateCard(UUID userId, UUID cardId, String newOwner);

    List<Card> saveCards(UUID id, List<Card> cards);

    void deleteCardById(UUID userId, UUID cardId);

    void deleteCardsByIds(UUID userId, List<UUID> cardsIds);

    void deleteCardsByUserId(UUID id);

    List<Card> findCardsByUserId(UUID id);

    Page<Card> findCardsByCriteria(UUID userId, CardSearchCriteriaDto cardSearchCriteriaDto,
                                                    Pageable pageable);

    Card findUserCard(UUID userId, UUID cardId);

    Money getDepositByCardId(UUID userId, UUID id);

    void blockRequest(UUID cardId, UUID userId);
}
