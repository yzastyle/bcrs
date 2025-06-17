package com.quest.bank_card.service;

import com.quest.bank_card.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CardManagementService {

    Card saveCard(Card card);

    List<Card> saveAllCards(List<Card> cards);

    void deleteCardById(UUID id);

    void deleteCardsByIds(List<UUID> ids);

    List<Card> findAllByIds(List<UUID> ids);

    Card findCardById(UUID id);

    Card findByIdWithLock(UUID id);

    List<Card> findAllCards();

    Page<Card> findCardsByCriteria(Specification<Card> specification, Pageable pageable);

    void updateCardStatusById(UUID id, String status);

    boolean isCardOwnedBy(UUID cardId, UUID userId);

    List<Card> findByUserId(UUID id);

    public void validateAndUpdateExpiredCard(UUID cardId);
}
