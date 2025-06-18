package com.quest.bank_card.service.impl;

import com.quest.bank_card.BankCardApplicationTests;
import com.quest.bank_card.dto.CardCreateDto;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.model.Status;
import com.quest.bank_card.repository.CardRepository;
import com.quest.bank_card.service.CardGenerationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class CardGenerationServiceImplTest extends BankCardApplicationTests {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/yy");

    @Autowired
    CardGenerationService cardGenerationService;

    @Autowired
    CardRepository cardRepository;

    @Test
    public void generateCardTest() {
        CardCreateDto cardCreateDto = new CardCreateDto();
        cardCreateDto.setOwner("карта ДЛя нОвОгО владельЦа");
        Card card = cardGenerationService.generateCard(cardCreateDto);

        assertFalse(cardRepository.existsByNumber(card.getNumber()));
        assertEquals("KARTA DLYA NOVOGO VLADELTSA", card.getOwner());
        assertEquals(LocalDate.now().plusYears(4).format(DATE_FORMATTER),
                card.getExpirationDate());
        assertEquals(Status.ACTIVE, card.getStatus());
        assertEquals(new BigDecimal("0.00"), card.getDeposit().getAmount());
    }
}
