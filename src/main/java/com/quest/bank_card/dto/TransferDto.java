package com.quest.bank_card.dto;

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
    private BigDecimal amount;
}
