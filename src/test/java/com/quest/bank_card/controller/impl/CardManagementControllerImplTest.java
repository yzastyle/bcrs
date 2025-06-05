package com.quest.bank_card.controller.impl;

import com.quest.bank_card.dto.CardResponseDto;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.model.Status;
import com.quest.bank_card.service.CardManagementService;
import com.quest.bank_card.service.CardMapperService;
import com.quest.bank_card.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CardManagementControllerImplTest {

    private MockMvc mockMvc;
    @Mock
    private CardManagementService cardManagementService;
    @Mock
    private CardMapperService cardMapperService;
    @Mock
    private ValidationService validationService;
    @InjectMocks
    private CardManagementControllerImpl cardManagementController;
    private UUID testCardId1;
    private UUID testCardId2;
    private Card testCard1;
    private Card testCard2;
    private CardResponseDto testCardDto1;
    private CardResponseDto testCardDto2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(cardManagementController)
                .build();

        testCardId1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        testCardId2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");

        testCard1 = Card.builder()
                .id(testCardId1)
                .number("1234567890123456")
                .owner("IVAN PETROV")
                .expirationDate("12/28")
                .status(Status.ACTIVE)
                .deposit(new Money(new BigDecimal("1000.50")))
                .dateCreate(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();

        testCard2 = Card.builder()
                .id(testCardId2)
                .number("9876543210987654")
                .owner("ANNA IVANOVA")
                .expirationDate("06/29")
                .status(Status.BLOCKED)
                .deposit(new Money(new BigDecimal("500")))
                .dateCreate(LocalDateTime.of(2025, 1, 2, 12, 0))
                .build();

        testCardDto1 = CardResponseDto.builder()
                .id(testCardId1)
                .number("1234567890123456")
                .owner("IVAN PETROV")
                .expirationDate("12/28")
                .status(Status.ACTIVE)
                .deposit(BigDecimal.valueOf(1000.50))
                .userId(UUID.fromString("123e4567-e89b-12d3-a456-426614174101"))
                .dateCreate(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();
    }

    @Test
    void getAllCardsTest() throws Exception {
        List<Card> cards = Arrays.asList(testCard1, testCard2);
        List<CardResponseDto> cardDtos = Arrays.asList(testCardDto1, testCardDto2);
        when(cardManagementService.findAllCards()).thenReturn(cards);
        when(cardMapperService.toDtoList(cards)).thenReturn(cardDtos);

        mockMvc.perform(get("/api/v1/cards")
                        .accept("application/json;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(testCardId1.toString()))
                .andExpect(jsonPath("$[0].number").value("1234567890123456"))
                .andExpect(jsonPath("$[0].owner").value("IVAN PETROV"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));

        verify(cardManagementService, times(1)).findAllCards();
        verify(cardMapperService, times(1)).toDtoList(cards);
    }

    @Test
    void getCardByIdTest() throws Exception {
        when(cardManagementService.findCardById(testCardId1)).thenReturn(testCard1);
        when(cardMapperService.toDto(testCard1)).thenReturn(testCardDto1);

        mockMvc.perform(get("/api/v1/cards/{id}", testCardId1)
                        .accept("application/json;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(testCardId1.toString()))
                .andExpect(jsonPath("$.number").value("1234567890123456"))
                .andExpect(jsonPath("$.owner").value("IVAN PETROV"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(cardManagementService, times(1)).findCardById(testCardId1);
        verify(cardMapperService, times(1)).toDto(testCard1);
    }

    @Test
    void getCardByIdTest_InvalidUUID() throws Exception {
        mockMvc.perform(get("/api/v1/cards/{id}", "invalid-uuid")
                        .accept("application/json;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(cardManagementService, never()).findCardById(any(UUID.class));
        verify(cardMapperService, never()).toDto(any(Card.class));
    }

    @Test
    void updateStatusTest() throws Exception {
        when(validationService.validateStatus("active")).thenReturn(Status.ACTIVE);
        doNothing().when(cardManagementService).UpdateCardStatusById(testCardId1, Status.ACTIVE);

        mockMvc.perform(patch("/api/v1/cards/{id}/{status}", testCardId1, "active"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string("The card status was successfully updated"));

        verify(validationService, times(1)).validateStatus("active");
        verify(cardManagementService, times(1)).UpdateCardStatusById(testCardId1, Status.ACTIVE);
    }

    @Test
    void updateStatusTest_InvalidUUID() throws Exception {
        mockMvc.perform(patch("/api/v1/cards/{id}/{status}", "invalid-uuid", "active"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(validationService, never()).validateStatus(any());
        verify(cardManagementService, never()).UpdateCardStatusById(any(UUID.class), any());
    }

    @Test
    void updateStatusTest_BlockStatus() throws Exception {
        when(validationService.validateStatus("blocked")).thenReturn(Status.BLOCKED);
        doNothing().when(cardManagementService).UpdateCardStatusById(testCardId1, Status.BLOCKED);

        mockMvc.perform(patch("/api/v1/cards/{id}/{status}", testCardId1, "blocked"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string("The card status was successfully updated"));

        verify(validationService, times(1)).validateStatus("blocked");
        verify(cardManagementService, times(1)).UpdateCardStatusById(testCardId1, Status.BLOCKED);
    }
}