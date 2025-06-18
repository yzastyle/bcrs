package com.quest.bank_card.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.bank_card.dto.TransferDto;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.exception.InsufficientFundsException;
import com.quest.bank_card.exception.ValidationException;
import com.quest.bank_card.exception.handler.GlobalExceptionHandler;
import com.quest.bank_card.service.CardManagementService;
import com.quest.bank_card.service.MoneyMapperService;
import com.quest.bank_card.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TransferControllerImplTest {

    private MockMvc mockMvc;
    @Mock
    private TransferService transferService;
    @Mock
    private CardManagementService cardManagementService;
    @Mock
    private MoneyMapperService moneyMapperService;
    @InjectMocks
    private TransferControllerImpl transferController;
    private ObjectMapper objectMapper;
    private UUID testUserId;
    private UUID testFromCardId;
    private UUID testToCardId;
    private TransferDto testTransferDto;
    private Money testMoney;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(transferController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        testUserId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        testFromCardId = UUID.fromString("123e4567-e89b-12d3-a456-426614174101");
        testToCardId = UUID.fromString("123e4567-e89b-12d3-a456-426614174102");

        testTransferDto = TransferDto.builder()
                .fromCardId(testFromCardId)
                .toCardId(testToCardId)
                .amount(new BigDecimal("500.00"))
                .build();

        testMoney = new Money(new BigDecimal("500.00"));
    }

    @Test
    public void transferTest() throws Exception {
        doNothing().when(cardManagementService).validateAndUpdateExpiredCard(testFromCardId);
        doNothing().when(cardManagementService).validateAndUpdateExpiredCard(testToCardId);
        when(moneyMapperService.toMoney(testTransferDto.getAmount())).thenReturn(testMoney);
        doNothing().when(transferService).transferBetweenUserCards(
                eq(testFromCardId), eq(testToCardId), eq(testUserId), eq(testMoney));

        mockMvc.perform(post("/api/v1/transfer/{userId}", testUserId)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(testTransferDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(content().string("Transfer completed successfully"));

        verify(cardManagementService, times(1)).validateAndUpdateExpiredCard(testFromCardId);
        verify(cardManagementService, times(1)).validateAndUpdateExpiredCard(testToCardId);
        verify(moneyMapperService, times(1)).toMoney(testTransferDto.getAmount());
        verify(transferService, times(1)).transferBetweenUserCards(
                testFromCardId, testToCardId, testUserId, testMoney);
    }

    @Test
    public void transferTestN_sameIdentifiers() throws Exception {
        ValidationException validationException = new ValidationException("Transfer canceled. The same card identifiers are specified");
        doNothing().when(cardManagementService).validateAndUpdateExpiredCard(testFromCardId);
        doNothing().when(cardManagementService).validateAndUpdateExpiredCard(testToCardId);
        when(moneyMapperService.toMoney(testTransferDto.getAmount())).thenReturn(testMoney);
        doThrow(validationException).when(transferService).transferBetweenUserCards(eq(testFromCardId), eq(testToCardId), eq(testUserId), eq(testMoney));

        mockMvc.perform(post("/api/v1/transfer/{userId}", testUserId)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(testTransferDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Transfer canceled. The same card identifiers are specified"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.statusCode").value(400));

        verify(cardManagementService, times(1)).validateAndUpdateExpiredCard(testFromCardId);
        verify(cardManagementService, times(1)).validateAndUpdateExpiredCard(testToCardId);
        verify(moneyMapperService, times(1)).toMoney(testTransferDto.getAmount());
        verify(transferService, times(1)).transferBetweenUserCards(
                testFromCardId, testToCardId, testUserId, testMoney);
    }

    @Test
    public void transferTestN_availableDeposit() throws Exception {
        InsufficientFundsException insufficientFundsException = new InsufficientFundsException(
                "Insufficient funds. Available: "
                + "500"
                + ", Required: "
                + "1000");
        doNothing().when(cardManagementService).validateAndUpdateExpiredCard(testFromCardId);
        doNothing().when(cardManagementService).validateAndUpdateExpiredCard(testToCardId);
        when(moneyMapperService.toMoney(testTransferDto.getAmount())).thenReturn(testMoney);
        doThrow(insufficientFundsException).when(transferService).transferBetweenUserCards(eq(testFromCardId), eq(testToCardId), eq(testUserId), eq(testMoney));

        mockMvc.perform(post("/api/v1/transfer/{userId}", testUserId)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(testTransferDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.errorCode").value("INSUFFICIENT_FUNDS_ERROR"))
                .andExpect(jsonPath("$.message").value("Insufficient funds. Available: 500, Required: 1000"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.statusCode").value(400));

        verify(cardManagementService, times(1)).validateAndUpdateExpiredCard(testFromCardId);
        verify(cardManagementService, times(1)).validateAndUpdateExpiredCard(testToCardId);
        verify(moneyMapperService, times(1)).toMoney(testTransferDto.getAmount());
        verify(transferService, times(1)).transferBetweenUserCards(
                testFromCardId, testToCardId, testUserId, testMoney);
    }

    @Test
    public void transferTestN_amountNull() throws Exception {
        TransferDto invalidDto = TransferDto.builder()
                .fromCardId(testFromCardId)
                .toCardId(testToCardId)
                .amount(null)
                .build();

        mockMvc.perform(post("/api/v1/transfer/{userId}", testUserId)
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
                .andExpect(jsonPath("$.details.amount").value("The amount must between not null"));

        verify(cardManagementService, never()).validateAndUpdateExpiredCard(any(UUID.class));
        verify(moneyMapperService, never()).toMoney(any(BigDecimal.class));
        verify(transferService, never()).transferBetweenUserCards(any(UUID.class), any(UUID.class), any(UUID.class), any());
    }

    @Test
    public void transferTestN_amountNegative() throws Exception {
        TransferDto invalidDto = TransferDto.builder()
                .fromCardId(testFromCardId)
                .toCardId(testToCardId)
                .amount(new BigDecimal("-1"))
                .build();

        mockMvc.perform(post("/api/v1/transfer/{userId}", testUserId)
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
                .andExpect(jsonPath("$.details.amount").value("The amount must be positive"));

        verify(cardManagementService, never()).validateAndUpdateExpiredCard(any(UUID.class));
        verify(moneyMapperService, never()).toMoney(any(BigDecimal.class));
        verify(transferService, never()).transferBetweenUserCards(any(UUID.class), any(UUID.class), any(UUID.class), any());
    }

    @Test
    public void transferTestN_amountZero() throws Exception {
        TransferDto invalidDto = TransferDto.builder()
                .fromCardId(testFromCardId)
                .toCardId(testToCardId)
                .amount(BigDecimal.ZERO)
                .build();

        mockMvc.perform(post("/api/v1/transfer/{userId}", testUserId)
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
                .andExpect(jsonPath("$.details.amount").value("The amount must be positive"));

        verify(cardManagementService, never()).validateAndUpdateExpiredCard(any(UUID.class));
        verify(moneyMapperService, never()).toMoney(any(BigDecimal.class));
        verify(transferService, never()).transferBetweenUserCards(any(UUID.class), any(UUID.class), any(UUID.class), any());
    }

    @Test
    public void transferTestN_amountTooManyDecimals() throws Exception {
        TransferDto invalidDto = TransferDto.builder()
                .fromCardId(testFromCardId)
                .toCardId(testToCardId)
                .amount(new BigDecimal("10.111"))
                .build();

        mockMvc.perform(post("/api/v1/transfer/{userId}", testUserId)
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
                .andExpect(jsonPath("$.details.amount").value("The amount must not contain more than 2 decimal places."));

        verify(cardManagementService, never()).validateAndUpdateExpiredCard(any(UUID.class));
        verify(moneyMapperService, never()).toMoney(any(BigDecimal.class));
        verify(transferService, never()).transferBetweenUserCards(any(UUID.class), any(UUID.class), any(UUID.class), any());
    }
}