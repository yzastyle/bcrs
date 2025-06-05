package com.quest.bank_card.service;

import com.quest.bank_card.dto.CardResponseDto;
import com.quest.bank_card.dto.MoneyDto;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardMapperService {

    public static final String maskedNumber = "**** **** **** ";

    public List<CardResponseDto> toDtoList(List<Card> cards) {
        return cards.stream().map(this::toDto).toList();
    }

    public CardResponseDto toDto(Card card) {
        return CardResponseDto.builder()
                .number(getMaskedNumber(card.getNumber()))
                .owner(card.getOwner())
                .deposit(card.getDeposit().getAmount())
                .expirationDate(card.getExpirationDate())
                .id(card.getId())
                .status(card.getStatus())
                .userId(card.getUser().getId())
                .dateCreate(card.getDateCreate())
                .build();
    }

    public MoneyDto toDto(Money money) {
        return MoneyDto.builder()
                .balance(money.getAmount()).build();
    }

    private String getMaskedNumber(String number) {
        return maskedNumber + number.substring(12);
    }
}
