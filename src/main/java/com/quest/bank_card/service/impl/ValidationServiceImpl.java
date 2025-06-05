package com.quest.bank_card.service.impl;

import com.quest.bank_card.dto.CardCreateDto;
import com.quest.bank_card.dto.RegistrationDto;
import com.quest.bank_card.dto.RequestJwtDto;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.entity.User;
import com.quest.bank_card.exception.ValidationException;
import com.quest.bank_card.model.Status;
import com.quest.bank_card.service.CardGenerationService;
import com.quest.bank_card.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    public static final Set<String> sortDirectionSet = Set.of("ASC", "DESC");

    private final CardGenerationService cardGenerationService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User validateUser(RegistrationDto registrationDto) {
        String error = validateLoginOrName(registrationDto.getLogin());
        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
        error = validateLoginOrName(registrationDto.getName());
        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }

        return new User(registrationDto.getLogin(), registrationDto.getName(),
                passwordEncoder.encode(registrationDto.getPassword()));
    }

    @Override
    public void validateRequestJwtDto(RequestJwtDto requestJwtDto) {
        String error = validateLoginOrName(requestJwtDto.getLogin());
        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
        error = validateLoginOrName(requestJwtDto.getName());
        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
    }

    @Override
    public Card validateCard(CardCreateDto cardCreateDto) {
        String error = validateLoginOrName(cardCreateDto.getOwner());
        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
        String ownerName = toUpperLatinOwnerName(cardCreateDto.getOwner());
        String cardNumber = cardGenerationService.generateUniqueCardNumber();
        String expirationDate = cardGenerationService.generateExpirationDate();


        return new Card(cardNumber, ownerName, expirationDate, Status.ACTIVE, new Money(new BigDecimal("0")));
    }

    @Override
    public String validateCard(String owner) {
        String error = validateLoginOrName(owner);
        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
        return toUpperLatinOwnerName(owner);
    }

    public Status validateStatus(String status) {
        status = status.toUpperCase();
        Status cardStatus;
        try {
            cardStatus = Status.valueOf(status);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalStateException("Status: " + status + " is not exists");
        }
        return cardStatus;
    }

    @Override
    public Sort.Direction validateSortDirection(String sortDirection) {
        sortDirection = sortDirection.toUpperCase();
        if (!sortDirectionSet.contains(sortDirection)) {
            throw new ValidationException("The specified parameter " + sortDirection + " is invalid.");
        }
        return sortDirection.equals("DESC") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
    }

    @Override
    public Money validateAmount(BigDecimal amount) {
        checkPositiveAndNotNull(amount);
        checkScale(amount);
        return new Money(amount);
    }

    private String validateLoginOrName(String userData) {
        String error = "";

        if (userData == null || userData.trim().isEmpty()) {
            error = "User info cannot be null or empty";
            return error;
        }
        if (userData.length() < 2) {
            error ="User info must be at least 2 characters long";
        }
        if (userData.length() > 50) {
            error = "User info cannot be longer than 50 characters";
        }
        return error;
    }

    private String toUpperLatinOwnerName(String ownerName) {
        String owner = ownerName.trim().replaceAll("\\s+", " ").toUpperCase();
        return transliterateToLatin(owner);
    }

    private String transliterateToLatin(String input) {
        return input
                .replace("А", "A").replace("Б", "B").replace("В", "V")
                .replace("Г", "G").replace("Д", "D").replace("Е", "E")
                .replace("Ё", "E").replace("Ж", "ZH").replace("З", "Z")
                .replace("И", "I").replace("Й", "Y").replace("К", "K")
                .replace("Л", "L").replace("М", "M").replace("Н", "N")
                .replace("О", "O").replace("П", "P").replace("Р", "R")
                .replace("С", "S").replace("Т", "T").replace("У", "U")
                .replace("Ф", "F").replace("Х", "KH").replace("Ц", "TS")
                .replace("Ч", "CH").replace("Ш", "SH").replace("Щ", "SCH")
                .replace("Ъ", "").replace("Ы", "Y").replace("Ь", "")
                .replace("Э", "E").replace("Ю", "YU").replace("Я", "YA");
    }

    private void checkPositiveAndNotNull(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Сумма не может быть null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма должна быть больше нуля");
        }
    }

    private void checkScale(BigDecimal amount) {
        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Сумма не должна содержать более 2 знаков после запятой");
        }
    }
}
