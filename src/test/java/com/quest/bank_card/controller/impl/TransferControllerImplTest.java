package com.quest.bank_card.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.bank_card.dto.TransferDto;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.exception.ExpiredStatusCardException;
import com.quest.bank_card.service.TransferService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TransferControllerImplTest {

    private MockMvc mockMvc;
    @Mock
    private TransferService transferService;
    @Mock
    private ValidationService validationService;
    @InjectMocks
    private TransferControllerImpl transferController;
    private ObjectMapper objectMapper;
    private UUID testUserId;
    private UUID testFromCardId;
    private UUID testToCardId;
    private TransferDto testTransferDto;
    private Money testMoney;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(transferController)
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
    void transferTest() throws Exception {
        when(validationService.validateAmount(any(BigDecimal.class))).thenReturn(testMoney);
        doNothing().when(transferService).transferBetweenUserCards(
                eq(testFromCardId), eq(testToCardId), eq(testUserId), eq(testMoney));

        mockMvc.perform(post("/api/v1/transfer/{userId}", testUserId)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(testTransferDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(content().string("Transfer completed successfully"));

        verify(validationService, times(1)).validateAmount(testTransferDto.getAmount());
        verify(transferService, times(1)).transferBetweenUserCards(
                testFromCardId, testToCardId, testUserId, testMoney);
    }

    @Test
    void transferTest_ExpiredStatusCardException() throws Exception {
        String errorMessage = "card is expired";
        when(validationService.validateAmount(any(BigDecimal.class))).thenReturn(testMoney);
        doThrow(new ExpiredStatusCardException(errorMessage))
                .when(transferService).transferBetweenUserCards(
                        eq(testFromCardId), eq(testToCardId), eq(testUserId), eq(testMoney));

        mockMvc.perform(post("/api/v1/transfer/{userId}", testUserId)
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(testTransferDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(content().string(errorMessage));

        verify(validationService, times(1)).validateAmount(testTransferDto.getAmount());
        verify(transferService, times(1)).transferBetweenUserCards(
                testFromCardId, testToCardId, testUserId, testMoney);
    }

    @Test
    void transferTest_InvalidUserId() throws Exception {
        mockMvc.perform(post("/api/v1/transfer/{userId}", "invalid-uuid")
                        .contentType("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(testTransferDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(validationService, never()).validateAmount(any());
        verify(transferService, never()).transferBetweenUserCards(any(), any(), any(), any());
    }
}