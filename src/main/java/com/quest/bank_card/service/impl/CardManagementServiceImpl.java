package com.quest.bank_card.service.impl;

import com.quest.bank_card.entity.Card;
import com.quest.bank_card.exception.CardNotFoundException;
import com.quest.bank_card.repository.CardRepository;
import com.quest.bank_card.service.CardManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardManagementServiceImpl implements CardManagementService {

    private final CardRepository cardRepository;

    @Override
    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public List<Card> saveAllCards(List<Card> cards) {
        return cardRepository.saveAll(cards);
    }

    @Override
    public void deleteCardById(UUID id) {
        cardRepository.deleteById(id);
    }

    @Override
    public void deleteCardsByIds(List<UUID> ids) {
        cardRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public List<Card> findAllByIds(List<UUID> ids) {
        return cardRepository.findAllById(ids);
    }

    @Override
    public Card findCardById(UUID id) {
        return cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
    }

    @Override
    public List<Card> findAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public Page<Card> findCardsByCriteria(Specification<Card> specification, Pageable pageable) {
        return cardRepository.findAll(specification, pageable);
    }

    @Override
    public void UpdateCardStatusById(UUID id, String status) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        card.updateStatus(status);
        cardRepository.save(card);
    }

    @Override
    public boolean isCardOwnedBy(UUID cardId, UUID userId) {
          return cardRepository.existsByIdAndUserId(cardId, userId);
    }

    @Override
    public List<Card> findByUserId(UUID id) {
        return cardRepository.findByUserId(id);
    }
}
