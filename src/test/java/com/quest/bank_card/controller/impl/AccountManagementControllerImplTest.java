package com.quest.bank_card.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.bank_card.dto.*;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.model.Status;
import com.quest.bank_card.service.*;
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
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AccountManagementControllerImplTest {

    private MockMvc mockMvc;
    @Mock
    private CardMapperService cardMapperService;
    @Mock
    private ValidationService validationService;
    @Mock
    private AccountManagementService accountManagementService;
    @InjectMocks
    private AccountManagementControllerImpl accountManagementController;
    private ObjectMapper objectMapper;
    private UUID testUserId;
    private UUID testCardId1;
    private UUID testCardId2;
    private Card testCard1;
    private CardResponseDto testCardDto1;
    private CardCreateDto testCardCreateDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(accountManagementController)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        testUserId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        testCardId1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174101");
        testCardId2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174102");

        testCardCreateDto = new CardCreateDto();
        testCardCreateDto.setOwner("IVAN PETROV");

        testCard1 = Card.builder()
                .id(testCardId1)
                .number("1234567890123456")
                .owner("IVAN PETROV")
                .expirationDate("12/28")
                .status(Status.ACTIVE)
                .deposit(new Money(new BigDecimal("1000.50")))
                .dateCreate(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();

        testCardDto1 = CardResponseDto.builder()
                .id(testCardId1)
                .number("1234567890123456")
                .owner("IVAN PETROV")
                .expirationDate("12/28")
                .status(Status.ACTIVE)
                .deposit(BigDecimal.valueOf(1000.50))
                .userId(testUserId)
                .dateCreate(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();
    }

    @Test
    void createCardTest() throws Exception {
        when(validationService.validateCard(any(CardCreateDto.class))).thenReturn(testCard1);
        when(accountManagementService.saveCard(any(UUID.class), any(Card.class))).thenReturn(testCard1);
        when(cardMapperService.toDto(testCard1)).thenReturn(testCardDto1);

        mockMvc.perform(post("/api/v1/account/{userId}/card", testUserId)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(testCardCreateDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(testCardId1.toString()))
                .andExpect(jsonPath("$.owner").value("IVAN PETROV"));

        verify(validationService, times(1)).validateCard(any(CardCreateDto.class));
        verify(accountManagementService, times(1)).saveCard(testUserId, testCard1);
        verify(cardMapperService, times(1)).toDto(testCard1);
    }

    @Test
    void createCardTest_InvalidUserId() throws Exception {
        mockMvc.perform(post("/api/v1/account/{userId}/card", "invalid-uuid")
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(testCardCreateDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(validationService, never()).validateCard((CardCreateDto) any());
        verify(accountManagementService, never()).saveCard(any(), any());
        verify(cardMapperService, never()).toDto(any(Card.class));
    }

    @Test
    void updateCardTest() throws Exception {
        when(validationService.validateCard("IVAN PETROV")).thenReturn("IVAN PETROV");
        when(accountManagementService.updateCard(testUserId, testCardId1, "IVAN PETROV")).thenReturn(testCard1);
        when(cardMapperService.toDto(testCard1)).thenReturn(testCardDto1);

        mockMvc.perform(put("/api/v1/account/{userId}/card/{cardId}", testUserId, testCardId1)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(testCardCreateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(testCardId1.toString()));

        verify(validationService, times(1)).validateCard("IVAN PETROV");
        verify(accountManagementService, times(1)).updateCard(testUserId, testCardId1, "IVAN PETROV");
        verify(cardMapperService, times(1)).toDto(testCard1);
    }

    @Test
    void updateCardTest_InvalidCardId() throws Exception {
        mockMvc.perform(put("/api/v1/account/{userId}/card/{cardId}", testUserId, "invalid-uuid")
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(testCardCreateDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(validationService, never()).validateCard((CardCreateDto) any());
        verify(accountManagementService, never()).updateCard(any(), any(), any());
    }

    @Test
    void deleteCardByIdTest() throws Exception {
        doNothing().when(accountManagementService).deleteCardById(testUserId, testCardId1);

        mockMvc.perform(delete("/api/v1/account/{userId}/card/{cardId}", testUserId, testCardId1))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(accountManagementService, times(1)).deleteCardById(testUserId, testCardId1);
    }

    @Test
    void deleteCardByIdTest_InvalidUserId() throws Exception {
        mockMvc.perform(delete("/api/v1/account/{userId}/card/{cardId}", "invalid-uuid", testCardId1))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(accountManagementService, never()).deleteCardById(any(), any());
    }

    @Test
    void deleteCardsByIdsTest() throws Exception {
        List<UUID> cardIds = Arrays.asList(testCardId1, testCardId2);
        doNothing().when(accountManagementService).deleteCardsByIds(testUserId, cardIds);

        mockMvc.perform(delete("/api/v1/account/{userId}/cards", testUserId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cardIds)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(accountManagementService, times(1)).deleteCardsByIds(testUserId, cardIds);
    }

    @Test
    void deleteCardsByUserIdTest() throws Exception {
        doNothing().when(accountManagementService).deleteCardsByUserId(testUserId);

        mockMvc.perform(delete("/api/v1/account/{userId}/cards/full", testUserId))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(accountManagementService, times(1)).deleteCardsByUserId(testUserId);
    }
}