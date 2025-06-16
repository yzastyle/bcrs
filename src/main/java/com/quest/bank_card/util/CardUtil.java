package com.quest.bank_card.util;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CardUtil {

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

    public static String toUpperLatinOwnerName(String ownerName) {
        String owner = ownerName.trim().replaceAll("\\s+", " ").toUpperCase();
        return transliterateToLatin(owner);
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
