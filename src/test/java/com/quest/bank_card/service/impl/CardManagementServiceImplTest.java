package com.quest.bank_card.service.impl;

import com.quest.bank_card.BankCardApplicationTests;
import com.quest.bank_card.dto.CardSearchCriteriaDto;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.entity.User;
import com.quest.bank_card.exception.CardNotFoundException;
import com.quest.bank_card.model.Status;
import com.quest.bank_card.repository.UserRepository;
import com.quest.bank_card.repository.specification.CardSpecifications;
import com.quest.bank_card.service.CardManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CardManagementServiceImplTest extends BankCardApplicationTests {

    @Autowired
    CardManagementService cardManagementService;
    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    public void getUserForTest() {
        user = userRepository.findById(UUID.fromString("d17ba058-3684-41cc-9cdb-3ea95d0a9d6f")).get();
    }

    @Test
    public void createCardTest() {
        Card card = new Card("4111222233334411", "owner",
                "12/29", Status.ACTIVE, new Money(new BigDecimal(1000)));
        card.setUser(user);
        card = cardManagementService.saveCard(card);

        assertNotNull(card.getId());
    }

    @Test
    public void UpdateCardTest() {
        Card card = cardManagementService.findCardById(UUID.fromString("1b52679d-1c73-46a2-95ff-0ea5756f2513"));
        card.setOwner("new owner");
        card = cardManagementService.saveCard(card);

        assertEquals("new owner", card.getOwner());
    }

    @Test
    public void createCardsTest() {
        Card firstCard = new Card("6411222233334411", "owner",
                "12/29", Status.ACTIVE, new Money(new BigDecimal(1000)));
        Card secondCard = new Card("6431222233334411", "owner",
                "12/29", Status.ACTIVE, new Money(new BigDecimal(1000)));
        firstCard.setUser(user);
        secondCard.setUser(user);

        List<Card> cards = cardManagementService.saveAllCards(List.of(firstCard, secondCard));

        assertNotNull(cards);
        assertNotEquals(0, cards.size());
        assertNotNull(cards.get(0).getId());
    }

    @Test
    public void UpdateCardsTest() {
        Card firstCard = new Card("6411222233334411", "owner",
                "12/29", Status.ACTIVE, new Money(new BigDecimal(1000)));
        Card secondCard = new Card("6431222233334411", "owner",
                "12/29", Status.ACTIVE, new Money(new BigDecimal(1000)));
        firstCard.setUser(user);
        secondCard.setUser(user);

        List<Card> cards = cardManagementService.saveAllCards(List.of(firstCard, secondCard));

        assertNotNull(cards);
        assertNotEquals(0, cards.size());
        assertNotNull(cards.get(0).getId());
    }

    @Test
    public void deleteCardTest() {
        cardManagementService.deleteCardById(UUID.fromString("ff8d0496-46d7-4264-8570-df21b68ed5ff"));

        assertThrows(CardNotFoundException.class,
                () -> cardManagementService.findCardById(UUID.fromString("ff8d0496-46d7-4264-8570-df21b68ed5ff")));
    }

    @Test
    public void deletesCardTest() {
        List<UUID> ids = List.of(UUID.fromString("1f8d0496-46d7-4264-8570-df21b68ed5ff"),
                UUID.fromString("2b52679d-1c73-46a2-95ff-0ea5756f2513"));
        cardManagementService.deleteCardsByIds(ids);

        assertThrows(CardNotFoundException.class,
                () -> cardManagementService.findCardById(UUID.fromString("1f8d0496-46d7-4264-8570-df21b68ed5ff")));
        assertThrows(CardNotFoundException.class,
                () -> cardManagementService.findCardById(UUID.fromString("2b52679d-1c73-46a2-95ff-0ea5756f2513")));
    }

    @Test
    public void findAllCardsTest() {
        List<Card> cards = cardManagementService.findAllCards();

        assertNotNull(cards);
        assertNotEquals(0, cards.size());
    }

    @Test
    public void UpdateCardStatusTest() {
        UUID cardId = UUID.fromString("1b52679d-1c73-46a2-95ff-0ea5756f2513");
        cardManagementService.UpdateCardStatusById(cardId , Status.ACTIVE);

        assertEquals(Status.ACTIVE, cardManagementService.findCardById(cardId).getStatus());
    }

    @Test
    public void UpdateSameCardStatusTest() {
        assertThrows(IllegalStateException.class,
                () -> cardManagementService.UpdateCardStatusById(UUID.fromString("1b52679d-1c73-46a2-95ff-0ea5756f2513"), Status.EXPIRED));
    }

    @Test
    public void isCardOwnedByPositiveTest() {
        assertTrue(cardManagementService.isCardOwnedBy(UUID.fromString("1b52679d-1c73-46a2-95ff-0ea5756f2513"),
                UUID.fromString("d17ba058-3684-41cc-9cdb-3ea95d0a9d6f")));
    }

    @Test
    public void isCardOwnedByNegativeTest() {
        assertFalse(cardManagementService.isCardOwnedBy(UUID.fromString("1b52679d-1c73-46a2-95ff-0ea5756f2513"),
                UUID.fromString("d17ba058-4684-41cc-9cdb-3ea95d0a9d6f")));
    }

    @Test
    public void findCardsByCriteriaTestEmpty() {
        Specification<Card> spec = CardSpecifications.withCriteria(
                UUID.fromString("d17ba058-3684-41cc-9cdb-3ea95d009000"),
                CardSearchCriteriaDto.empty()
        );
        Sort sort = Sort.by(Sort.Direction.DESC, "dateCreate");
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<Card> page = cardManagementService.findCardsByCriteria(spec, pageable);

        assertEquals(11, page.getTotalElements());
    }

    @Test
    public void findCardsByOneCriteriaTest() {
        CardSearchCriteriaDto oneCriteria = CardSearchCriteriaDto.builder()
                .status(Status.ACTIVE).build();

        Specification<Card> spec = CardSpecifications.withCriteria(
                UUID.fromString("d17ba058-3684-41cc-9cdb-3ea95d009000"),
                oneCriteria
        );
        Sort sort = Sort.by(Sort.Direction.DESC, "dateCreate");
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<Card> page = cardManagementService.findCardsByCriteria(spec, pageable);

        assertEquals(8, page.getTotalElements());
        page.getContent().forEach(card -> assertEquals(Status.ACTIVE, card.getStatus()));
    }

    @Test
    public void findCardsByMultiCriteriaTest() {
        CardSearchCriteriaDto MultipleCriteria = CardSearchCriteriaDto.builder()
                .status(Status.ACTIVE)
                .expirationDate("12/24")
                .build();

        Specification<Card> spec = CardSpecifications.withCriteria(
                UUID.fromString("d17ba058-3684-41cc-9cdb-3ea95d009000"),
                MultipleCriteria
        );
        Sort sort = Sort.by(Sort.Direction.DESC, "dateCreate");
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<Card> page = cardManagementService.findCardsByCriteria(spec, pageable);

        assertEquals(7, page.getTotalElements());
        page.getContent().forEach(card -> {
            assertEquals(Status.ACTIVE, card.getStatus());
            assertEquals("12/24", card.getExpirationDate());
        });
    }

}
