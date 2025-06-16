package com.quest.bank_card.service;

import com.quest.bank_card.entity.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MoneyMapperService {

    public Money toMoney(BigDecimal amount) {
        return new Money(amount);
    }
}
