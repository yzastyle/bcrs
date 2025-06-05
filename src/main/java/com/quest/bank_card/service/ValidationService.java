package com.quest.bank_card.service;

import com.quest.bank_card.dto.CardCreateDto;
import com.quest.bank_card.dto.RegistrationDto;
import com.quest.bank_card.dto.RequestJwtDto;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.entity.User;
import com.quest.bank_card.model.Status;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;

public interface ValidationService {

    User validateUser(RegistrationDto registrationDto);

    void validateRequestJwtDto(RequestJwtDto requestJwtDto);

    Card validateCard(CardCreateDto cardCreateDto);

    String validateCard(String owner);

    Status validateStatus(String status);

    Sort.Direction validateSortDirection(String sortDirection);

    Money validateAmount(BigDecimal amount);
}
