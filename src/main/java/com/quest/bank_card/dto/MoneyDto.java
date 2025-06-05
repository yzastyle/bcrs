package com.quest.bank_card.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class MoneyDto {
    private BigDecimal balance;
}
