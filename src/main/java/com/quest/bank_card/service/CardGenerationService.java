package com.quest.bank_card.service;

import com.quest.bank_card.dto.CardCreateDto;
import com.quest.bank_card.entity.Card;

public interface CardGenerationService {

    Card generateCard(CardCreateDto cardCreateDto);

}
