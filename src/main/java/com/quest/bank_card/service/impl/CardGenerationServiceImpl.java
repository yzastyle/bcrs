package com.quest.bank_card.service.impl;

import com.quest.bank_card.repository.CardRepository;
import com.quest.bank_card.service.CardGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CardGenerationServiceImpl implements CardGenerationService {

    private final CardRepository cardRepository;
    private static final SecureRandom random = new SecureRandom();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/yy");

    @Override
    public String generateUniqueCardNumber() {
        String cardNumber;
        int attempts = 0;
        int maxAttempts = 100;

        do {
            cardNumber = generateCardNumber();
            attempts++;

            if (attempts >= maxAttempts) {
                throw new RuntimeException("Unable to generate unique card number");
            }
        } while (cardRepository.existsByNumber(cardNumber));

        return cardNumber;
    }

    @Override
    public String generateExpirationDate() {
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
