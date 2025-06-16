package com.quest.bank_card.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
public class TransferDto {
    private UUID toCardId;
    private UUID fromCardId;

    @NotNull(message = "The amount must between not null")
    @Positive(message = "The amount must be positive")
    @Digits(integer = 10, fraction = 2, message = "The amount must not contain more than 2 decimal places.")
    private BigDecimal amount;
}
