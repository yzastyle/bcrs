package com.quest.bank_card.service.impl;

import com.quest.bank_card.exception.UnauthorizedException;
import com.quest.bank_card.BankCardApplicationTests;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.exception.CardNotFoundException;
import com.quest.bank_card.exception.ValidationException;
import com.quest.bank_card.model.Status;
import com.quest.bank_card.service.AccountManagementService;
import com.quest.bank_card.service.CardManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountManagementServiceImplTest extends BankCardApplicationTests {

    @Autowired
    AccountManagementService accountManagementService;
    @Autowired
    CardManagementService cardManagementService;

    @Test
    public void saveCardTest() {
        UUID userId = UUID.fromString("d17ba058-3684-41cc-9cdb-3ea95d0a9d6f");
        Card card = new Card("4111643233334411", "owner",
                "12/29", Status.ACTIVE, new Money(new BigDecimal(1000)));
        card = accountManagementService.saveCard(userId, card);
        UUID cardId = card.getId();

        assertNotNull(cardId);

        List<Card> cards = cardManagementService.findCardsByUserId(userId);
        assertNotEquals(0, cards.size());
        assertEquals(cardId, cards.get(cards.size() - 1).getId());
    }

    @Test
    public void saveCardsTest() {
        Card firstCard = new Card("4111643244334411", "owner",
                "12/29", Status.ACTIVE, new Money(new BigDecimal(1000)));
        Card secondCard = new Card("4111643236634411", "owner",
                "12/29", Status.ACTIVE, new Money(new BigDecimal(1000)));
        List<Card> cards = accountManagementService.saveCards(UUID.fromString("d17ba058-3684-41cc-9cdb-3ea95d0a9d6f"),
                List.of(firstCard, secondCard));

        assertNotEquals(0, cards.size());
    }

    @Test
    public void deleteCardByIdTestN_nonZeroBalance() {
        assertThrows(ValidationException.class,
                () -> accountManagementService.deleteCardById(UUID.fromString("aaaaaab1-15a3-4a5f-8f0c-1a2dfc80cc6a"),
                        UUID.fromString("aaad0000-46d7-4264-8570-df21b68ed5ff")));
    }

    @Test
    public void deleteCardByIdTest() {
        accountManagementService.deleteCardById(UUID.fromString("aaaaaab1-15a3-4a5f-8f0c-1a2dfc80cc6a"),
                UUID.fromString("aaad0000-46d7-4264-8137-df21b68ed5ff"));

        assertThrows(CardNotFoundException.class,
                () -> cardManagementService.findCardById(UUID.fromString("aaad0000-46d7-4264-8137-df21b68ed5ff")));
    }

    @Test
    public void deleteCardByIdTestN_doesNotBelongToUser() {
        assertThrows(UnauthorizedException.class,
                () -> accountManagementService.deleteCardById(UUID.fromString("aaaaaa24-15a3-4a5f-8f0c-1a2dfc80cc6a"),
                        UUID.fromString("aaad0001-46d7-4264-8570-df21b68ed5ff")));
    }

    @Test
    public void deleteCardsByIdPositiveTest() {
        accountManagementService.deleteCardsByIds(UUID.fromString("10f80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a"),
                List.of(UUID.fromString("bbbd0496-46d7-1155-6670-df21b68ed5ff"),
                        UUID.fromString("bbbd0496-35d0-3334-8570-df21b68ed5ff")));

        assertThrows(CardNotFoundException.class,
                () -> cardManagementService.findCardById(UUID.fromString("bbbd0496-35d0-3334-8570-df21b68ed5ff")));
    }

    @Test
    public void deleteCardsByIdNegativeTest() {
        assertThrows(UnauthorizedException.class,
                () -> accountManagementService.deleteCardsByIds(UUID.fromString("d17ba058-3684-41cc-9cdb-3ea95d009000"),
                        List.of(UUID.fromString("1b52679d-1c11-66a2-95ff-0ea5756f2513"),
                                UUID.fromString("1b52679d-1c73-46a2-11ff-1ea5756f2513"))));
    }

    @Test
    public void deleteCardsByUserIdTest() {
        UUID userId = UUID.fromString("d0935ff9-54ea-4740-b32b-06da1f201121");
        accountManagementService.deleteCardsByUserId(userId);
        List<Card> cards = cardManagementService.findCardsByUserId(userId);
        assertEquals(0, cards.size());
    }

    @Test
    public void deleteCardsByUserIdTestN_nonZeroBalance() {
        UUID userId = UUID.fromString("b0e32aeb-a3f7-4c03-ba45-aa9bba16c934");

        assertThrows(ValidationException.class, () -> accountManagementService.deleteCardsByUserId(userId));
    }

    @Test
    public void findCardsByUserIdTest() {
        UUID id = UUID.fromString("d17ba058-3684-41cc-9cdb-3ea95d0a9d6f");
        List<Card> cards = accountManagementService.findCardsByUserId(id);

        assertNotEquals(0, cards.size());
        assertEquals(id, cards.get(0).getUser().getId());
    }

    @Test
    public void findUserCardTest() {
        UUID userId = UUID.fromString("d17ba058-3684-41cc-9cdb-3ea95d0a9d6f");
        UUID cardId = UUID.fromString("ff8d0496-46d7-4264-8570-df21b68ed5ff");
        Card card = accountManagementService.findUserCard(userId, cardId);

        assertNotNull(card);
        assertTrue(cardManagementService.isCardOwnedBy(card.getId(), userId));
    }

    @Test
    public void getDepositByCardIdTest() {
        UUID userId = UUID.fromString("aaaaaa24-15a3-1a23-8f0c-1a2dfc80cc6a");
        UUID cardId = UUID.fromString("faad0001-46d7-4264-8570-df21b68ed5ff");
        Money money = accountManagementService.getDepositByCardId(userId, cardId);

        assertEquals(0, money.getAmount().compareTo(new BigDecimal("1000")));
    }

    @Test
    public void blockRequestTest() {
        UUID userId = UUID.fromString("aaaaaa24-15a3-1a23-8f0c-1a2dfc80cc6a");
        UUID cardId = UUID.fromString("faad0001-46d7-4264-8570-df21b68ed5ff");

        accountManagementService.blockRequest(userId, cardId);
        assertEquals(Status.BLOCKED, cardManagementService.findCardById(cardId).getStatus());

    }
}
