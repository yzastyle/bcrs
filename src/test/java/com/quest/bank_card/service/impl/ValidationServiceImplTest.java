package com.quest.bank_card.service.impl;

import com.quest.bank_card.BankCardApplicationTests;
import com.quest.bank_card.dto.CardCreateDto;
import com.quest.bank_card.dto.RegistrationDto;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.User;
import com.quest.bank_card.exception.ValidationException;
import com.quest.bank_card.model.Status;
import com.quest.bank_card.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationServiceImplTest extends BankCardApplicationTests {

    @Autowired
    ValidationService validationService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/yy");

    @Test
    public void validateUserPositiveTest() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("te");
        registrationDto.setName("te");
        registrationDto.setPassword("testPass");

       User user = validationService.validateUser(registrationDto);

        assertInstanceOf(User.class, user);
       assertNotEquals("testPass", user.getPassword());
    }

    @Test
    public void validateUserNegativeTest_nullLogin() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin(null);
        registrationDto.setName("testName");
        registrationDto.setPassword("testPass");

        assertThrows(ValidationException.class, () -> validationService.validateUser(registrationDto));
    }

    @Test
    public void validateUserNegativeTest_nullName() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("test");
        registrationDto.setName(null);
        registrationDto.setPassword("testPass");

        assertThrows(ValidationException.class, () -> validationService.validateUser(registrationDto));
    }

    @Test
    public void validateUserNegativeTest_shortLogin() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("t");
        registrationDto.setName("te");
        registrationDto.setPassword("testPass");

        assertThrows(ValidationException.class, () -> validationService.validateUser(registrationDto));
    }

    @Test
    public void validateUserNegativeTest_shortName() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("tyyy");
        registrationDto.setName("t");
        registrationDto.setPassword("testPass");

        assertThrows(ValidationException.class, () -> validationService.validateUser(registrationDto));
    }

    @Test
    public void validateUserNegativeTest_longLogin() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("ddddddddddddddddddddddddddddddddddddddddddddddddddd");
        registrationDto.setName("teddd");
        registrationDto.setPassword("testPass");

        assertThrows(ValidationException.class, () -> validationService.validateUser(registrationDto));
    }

    @Test
    public void validateUserNegativeTest_longName() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("ddddd");
        registrationDto.setName("ddddddddddddddddddddddddddddddddddddddddddddddddddd");
        registrationDto.setPassword("testPass");

        assertThrows(ValidationException.class, () -> validationService.validateUser(registrationDto));
    }

    @Test
    public void validateCardPositiveTest() {
        CardCreateDto cardCreateDto = new CardCreateDto();
        cardCreateDto.setOwner("Саша Сашин");

        Card card = validationService.validateCard(cardCreateDto);

        assertInstanceOf(Card.class, card);

        assertEquals(Status.ACTIVE, card.getStatus());
        assertEquals("SASHA SASHIN", card.getOwner());
        assertEquals(LocalDate.now().plusYears(4).format(DATE_FORMATTER), card.getExpirationDate());
        assertNotNull(card.getNumber());
        assertEquals(new BigDecimal("0.00"), card.getDeposit().getAmount());
    }

    @Test
    public void validateCardNegativeTest_nullOwner() {
        CardCreateDto cardCreateDto = new CardCreateDto();
        cardCreateDto.setOwner(null);

        assertThrows(ValidationException.class, () -> validationService.validateCard(cardCreateDto));
    }

    @Test
    public void validateCardNegativeTest_shortOwner() {
        CardCreateDto cardCreateDto = new CardCreateDto();
        cardCreateDto.setOwner("d");

        assertThrows(ValidationException.class, () -> validationService.validateCard(cardCreateDto));
    }

    @Test
    public void validateUserNegativeTest_longOwner() {
        CardCreateDto cardCreateDto = new CardCreateDto();
        cardCreateDto.setOwner("ddddddddddddddddddddddddddddddddddddddddddddddddddd");

        assertThrows(ValidationException.class, () -> validationService.validateCard(cardCreateDto));
    }

    @Test
    public void validateAmountNegativeTest_null() {
        assertThrows(IllegalArgumentException.class, () -> validationService.validateAmount(null));
    }

    @Test
    public void validateAmountNegativeTest_negative() {
        assertThrows(IllegalArgumentException.class, () -> validationService.validateAmount(new BigDecimal("0")));
    }

    @Test
    public void validateAmountNegativeTest_scale() {
        assertThrows(IllegalArgumentException.class, () -> validationService.validateAmount(new BigDecimal("123123.111")));
    }
}
