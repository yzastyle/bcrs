package com.quest.bank_card.util;

import com.quest.bank_card.exception.ValidationException;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for working with bank card data.
 * Provides static methods for validating and processing card information,
 * including checking card expiration dates and normalizing cardholder names
 * according to international banking standards.
 *
 * <p>Main features:
 * <ul>
 *   <li>Checking whether a card has expired</li>
 *   <li>Transliteration and normalization of cardholder names</li>
 *   <li>Validation of card data formats</li>
 * </ul>
 *
 * <p>All methods are static and thread-safe.
 * The class uses immutable objects and holds no state.
 *
 * @author Devon
 */
public final class CardUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/yy");

    private CardUtil(){}

    /**
     * <p><strong>Important:</strong> A card remains valid through the end of its expiration month.
     * For example, a card with an expiration date of "01/25" is valid until the last day of January 2025.
     *
     * @param expirationDate the card’s expiration date in MM/yy format; must not be null or empty
     * @return {@code true} if the card has expired; {@code false} otherwise
     * @throws ValidationException if the expiration date format is invalid
     */
    public static boolean isExpired(String expirationDate) {
        YearMonth expirationMonth = parseToMonthYear(expirationDate);
        YearMonth currentMonth = YearMonth.now();

        return currentMonth.isAfter(expirationMonth);
    }

    public static String toUpperLatinOwnerName(String ownerName) {
        String owner = ownerName.trim().replaceAll("\\s+", " ").toUpperCase();
        return transliterateToLatin(owner);
    }

    private static YearMonth parseToMonthYear(String expirationDate) {
        if (expirationDate == null || expirationDate.trim().isEmpty()) {
            throw new ValidationException("Expiration date cannot be null or empty");
        }
        try {
            return YearMonth.parse(expirationDate.trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Invalid expiration date format: " + expirationDate +
                    ". Expected format: MM/yy (e.g., 08/26)");
        }
    }

    private static String transliterateToLatin(String input) {
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
}
