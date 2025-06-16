package com.quest.bank_card.exception;

import java.util.UUID;

public class CardNotFoundException extends BaseException {

    public static final String ERROR_CODE = "CARD_NOT_FOUND_ERROR";

    public CardNotFoundException(UUID id) {
        super(ERROR_CODE, "Card with id=" + id + " not found");
    }
}
