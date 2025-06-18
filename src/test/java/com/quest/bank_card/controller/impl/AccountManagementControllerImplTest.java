package com.quest.bank_card.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.bank_card.dto.CardCreateDto;
import com.quest.bank_card.dto.CardResponseDto;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.exception.UnauthorizedException;
import com.quest.bank_card.exception.ValidationException;
import com.quest.bank_card.exception.handler.GlobalExceptionHandler;
import com.quest.bank_card.model.Status;
import com.quest.bank_card.service.AccountManagementService;
import com.quest.bank_card.service.CardGenerationService;
import com.quest.bank_card.service.CardMapperService;
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
class AccountManagementControllerImplTest {

    private MockMvc mockMvc;
    @Mock
    private CardMapperService cardMapperService;
    @Mock
    private AccountManagementService accountManagementService;
    @Mock
    private CardGenerationService cardGenerationService;
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
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(accountManagementController)
                .setControllerAdvice(new GlobalExceptionHandler())
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
    public void createCardTest_2chars() throws Exception {
        CardCreateDto validDto = new CardCreateDto();
        validDto.setOwner("AB");

        when(cardGenerationService.generateCard(any(CardCreateDto.class))).thenReturn(testCard1);
        when(accountManagementService.saveCard(any(UUID.class), any(Card.class))).thenReturn(testCard1);
        when(cardMapperService.toDto(testCard1)).thenReturn(testCardDto1);

        mockMvc.perform(post("/api/v1/account/{userId}/card", testUserId)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(testCardId1.toString()));

        verify(cardGenerationService, times(1)).generateCard(any(CardCreateDto.class));
        verify(accountManagementService, times(1)).saveCard(testUserId, testCard1);
        verify(cardMapperService, times(1)).toDto(testCard1);
    }

    @Test
    public void createCardTest_50chars() throws Exception {
        CardCreateDto validDto = new CardCreateDto();
        validDto.setOwner("A".repeat(50));

        when(cardGenerationService.generateCard(any(CardCreateDto.class))).thenReturn(testCard1);
        when(accountManagementService.saveCard(any(UUID.class), any(Card.class))).thenReturn(testCard1);
        when(cardMapperService.toDto(testCard1)).thenReturn(testCardDto1);

        mockMvc.perform(post("/api/v1/account/{userId}/card", testUserId)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(testCardId1.toString()));

        verify(cardGenerationService, times(1)).generateCard(any(CardCreateDto.class));
        verify(accountManagementService, times(1)).saveCard(testUserId, testCard1);
        verify(cardMapperService, times(1)).toDto(testCard1);
    }

    @Test
    public void createCardTestN_ownerNull() throws Exception {
        CardCreateDto invalidDto = new CardCreateDto();
        invalidDto.setOwner(null);

        mockMvc.perform(post("/api/v1/account/{userId}/card", testUserId)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.errorCode").value("ARGUMENT_VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.details.owner").value("Owner information must not be blank"));

        verify(cardGenerationService, never()).generateCard(any(CardCreateDto.class));
        verify(accountManagementService, never()).saveCard(any(UUID.class), any(Card.class));
        verify(cardMapperService, never()).toDto(any(Card.class));
    }

    @Test
    public void createCardTestN_ownerBlank() throws Exception {
        CardCreateDto invalidDto = new CardCreateDto();
        invalidDto.setOwner("   ");

        mockMvc.perform(post("/api/v1/account/{userId}/card", testUserId)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.owner").value("Owner information must not be blank"));

        verify(cardGenerationService, never()).generateCard(any(CardCreateDto.class));
        verify(accountManagementService, never()).saveCard(any(UUID.class), any(Card.class));
        verify(cardMapperService, never()).toDto(any(Card.class));
    }

    @Test
    public void createCardTestN_ownerTooShort() throws Exception {
        CardCreateDto invalidDto = new CardCreateDto();
        invalidDto.setOwner("A");

        mockMvc.perform(post("/api/v1/account/{userId}/card", testUserId)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.owner").value("Owner information must be between at 2 or 50 characters long"));

        verify(cardGenerationService, never()).generateCard(any(CardCreateDto.class));
        verify(accountManagementService, never()).saveCard(any(UUID.class), any(Card.class));
        verify(cardMapperService, never()).toDto(any(Card.class));
    }

    @Test
    public void createCardInvalidOwnerTooLong() throws Exception {
        CardCreateDto invalidDto = new CardCreateDto();
        invalidDto.setOwner("A".repeat(51));

        mockMvc.perform(post("/api/v1/account/{userId}/card", testUserId)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.owner").value("Owner information must be between at 2 or 50 characters long"));

        verify(cardGenerationService, never()).generateCard(any(CardCreateDto.class));
        verify(accountManagementService, never()).saveCard(any(UUID.class), any(Card.class));
        verify(cardMapperService, never()).toDto(any(Card.class));
    }

    @Test
    public void createCardTestN_invalidUserId() throws Exception {
        mockMvc.perform(post("/api/v1/account/{userId}/card", "invalid-uuid")
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(testCardCreateDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Method parameter 'userId': Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; Invalid UUID string: invalid-uuid"));

        verify(cardGenerationService, never()).generateCard(any(CardCreateDto.class));
        verify(accountManagementService, never()).saveCard(any(), any());
        verify(cardMapperService, never()).toDto(any(Card.class));
    }

    @Test
    public void updateCardTest() throws Exception {
        when(accountManagementService.updateCard(testUserId, testCardId1, "IVAN PETROV")).thenReturn(testCard1);
        when(cardMapperService.toDto(testCard1)).thenReturn(testCardDto1);

        mockMvc.perform(put("/api/v1/account/{userId}/card/{cardId}", testUserId, testCardId1)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(testCardCreateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(testCardId1.toString()));

        verify(accountManagementService, times(1)).updateCard(testUserId, testCardId1, "IVAN PETROV");
        verify(cardMapperService, times(1)).toDto(testCard1);
    }

    @Test
    public void updateCardTestN_notOwner() throws Exception {
        UnauthorizedException unauthorizedException = new UnauthorizedException("Card does not belong to user. Operation cancelled.");
        when(accountManagementService.updateCard(testUserId, testCardId1, "IVAN PETROV"))
                .thenThrow(unauthorizedException);

        mockMvc.perform(put("/api/v1/account/{userId}/card/{cardId}", testUserId, testCardId1)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(testCardCreateDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED_ERROR"))
                .andExpect(jsonPath("$.message").value("Card does not belong to user. Operation cancelled."))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.statusCode").value(401));

        verify(accountManagementService, times(1)).updateCard(testUserId, testCardId1, "IVAN PETROV");
        verify(cardMapperService, never()).toDto(any(Card.class));
    }

    @Test
    public void deleteCardByIdTest() throws Exception {
        doNothing().when(accountManagementService).deleteCardById(testUserId, testCardId1);

        mockMvc.perform(delete("/api/v1/account/{userId}/card/{cardId}", testUserId, testCardId1))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(accountManagementService, times(1)).deleteCardById(testUserId, testCardId1);
    }

    @Test
    public void deleteCardByIdTestN_nonZeroBalance() throws Exception {
        ValidationException validationException = new ValidationException("Cannot delete card id=" + testCardId1 + " with non-zero balance");
        doThrow(validationException).when(accountManagementService).deleteCardById(testUserId, testCardId1);

        mockMvc.perform(delete("/api/v1/account/{userId}/card/{cardId}", testUserId, testCardId1))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Cannot delete card id=123e4567-e89b-12d3-a456-426614174101 with non-zero balance"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    public void deleteCardsByIdsTest() throws Exception {
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
    public void deleteCardsByUserIdTest() throws Exception {
        doNothing().when(accountManagementService).deleteCardsByUserId(testUserId);

        mockMvc.perform(delete("/api/v1/account/{userId}/cards/full", testUserId))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(accountManagementService, times(1)).deleteCardsByUserId(testUserId);
    }
}