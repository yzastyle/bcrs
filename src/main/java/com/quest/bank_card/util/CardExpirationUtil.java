package com.quest.bank_card.util;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CardExpirationUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/yy");

    private static YearMonth parseToYearMonth(String expirationDate) {
        if (expirationDate == null || expirationDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Expiration date cannot be null or empty");
        }
        try {
            return YearMonth.parse(expirationDate.trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid expiration date format: " + expirationDate +
                    ". Expected format: MM/yy (e.g., 08/26)");
        }
    }

    public static boolean isExpired(String expirationDate) {
        YearMonth expirationMonth = parseToYearMonth(expirationDate);
        YearMonth currentMonth = YearMonth.now();

        return currentMonth.isAfter(expirationMonth);
    }
}
