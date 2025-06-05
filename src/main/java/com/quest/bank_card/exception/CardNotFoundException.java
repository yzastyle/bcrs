package com.quest.bank_card.exception;

import java.util.UUID;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(UUID id) {
        super("Card with id=" + id + " not found");
    }
}
