package com.quest.bank_card.service;

import com.quest.bank_card.entity.Money;

import java.util.UUID;

public interface TransferService {

    void transferBetweenUserCards(UUID fromCardId, UUID toCardId, UUID userId, Money amount);
}
