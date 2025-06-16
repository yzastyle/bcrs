package com.quest.bank_card.service.impl;

import com.quest.bank_card.BankCardApplicationTests;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.exception.ExpiredStatusCardException;
import com.quest.bank_card.exception.InsufficientFundsException;
import com.quest.bank_card.exception.ValidationException;
import com.quest.bank_card.service.CardManagementService;
import com.quest.bank_card.service.TransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TransferServiceImplTest extends BankCardApplicationTests {

    @Autowired
    TransferService transferService;
    @Autowired
    CardManagementService cardManagementService;

    @Test
    public void transferBetweenUserCardTest_invalidStatus() {
        UUID userId = UUID.fromString("d17ba058-3684-41cc-9cdb-3ea95d0a9d6f");
        UUID fromCardId = UUID.fromString("ff8d0496-46d7-4264-8570-df21b68ed5ff");
        UUID toCardId = UUID.fromString("1b52679d-1c73-46a2-95ff-0ea5756f2513");

        assertThrows(IllegalStateException.class, () -> transferService.transferBetweenUserCards(fromCardId, toCardId, userId, new Money(new BigDecimal("1000.00"))));
    }

    @Test
    public void transferBetweenUserCardTest() throws ExpiredStatusCardException {
        UUID userId = UUID.fromString("11111ab1-15a3-4a5f-8f0c-1a2df111cc6a");
        UUID fromCardId = UUID.fromString("9c05a73f-38a4-0000-91bf-0047dddfb70f");
        UUID toCardId = UUID.fromString("9c05a73f-11a1-0000-91bf-0047d48fb70f");

        transferService.transferBetweenUserCards(fromCardId, toCardId, userId, new Money(new BigDecimal("250.75")));

        assertEquals(0, cardManagementService.findCardById(toCardId).getDeposit().getAmount().compareTo(new BigDecimal("501.50")));
    }

    @Test
    public void transferBetweenUserCardTest_insufficientFunds() {
        UUID userId = UUID.fromString("82b547fa-18c6-4d40-b497-a1642e8aac2c");
        UUID fromCardId = UUID.fromString("b351ba45-f03b-48dd-ac62-c9f4fbae2707");
        UUID toCardId = UUID.fromString("dcd57932-0e43-4ae4-94eb-07a2bff90f29");

        assertThrows(InsufficientFundsException.class, () -> transferService.transferBetweenUserCards(fromCardId, toCardId, userId, new Money(new BigDecimal("1000.01"))));
    }

    @Test void transferBetweenUserCardTest_sameCardIds() {
        UUID userId = UUID.fromString("11111ab1-15a3-4a5f-8f0c-1a2df111cc6a");
        UUID fromCardId = UUID.fromString("9c05a73f-38a4-0000-91bf-0047dddfb70f");

        assertThrows(ValidationException.class, () -> transferService.transferBetweenUserCards(fromCardId, fromCardId, userId,new Money(new BigDecimal("250.75"))));
    }
}
