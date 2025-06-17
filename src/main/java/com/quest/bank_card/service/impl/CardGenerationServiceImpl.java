package com.quest.bank_card.service.impl;

import com.quest.bank_card.dto.CardCreateDto;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.exception.BaseException;
import com.quest.bank_card.model.Status;
import com.quest.bank_card.repository.CardRepository;
import com.quest.bank_card.service.CardGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.quest.bank_card.util.CardUtil.toUpperLatinOwnerName;

@Service
@RequiredArgsConstructor
public class CardGenerationServiceImpl implements CardGenerationService {

    private final CardRepository cardRepository;
    private static final SecureRandom random = new SecureRandom();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/yy");

    public Card generateCard(CardCreateDto cardCreateDto) {
        String ownerName = toUpperLatinOwnerName(cardCreateDto.getOwner());
        String cardNumber = generateUniqueCardNumber();
        String expirationDate = generateExpirationDate();

        return new Card(cardNumber, ownerName, expirationDate, Status.ACTIVE, new Money(new BigDecimal("0")));
    }

    private String generateUniqueCardNumber() {
        String cardNumber;
        int attempts = 0;
        int maxAttempts = 100;

        do {
            cardNumber = generateCardNumber();
            attempts++;

            if (attempts >= maxAttempts) {
                throw new BaseException("Unable to generate unique card number", HttpStatus.BAD_REQUEST) {
                };
            }
        } while (cardRepository.existsByNumber(cardNumber));

        return cardNumber;
    }

    private String generateExpirationDate() {
        LocalDate expirationDate = LocalDate.now().plusYears(4);
        return expirationDate.format(DATE_FORMATTER);
    }

    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }

        return cardNumber.toString();
    }
}
