package com.quest.bank_card.exception;

import org.springframework.http.HttpStatus;

public class InsufficientFundsException extends BaseException {

    public static final String ERROR_CODE = "INSUFFICIENT_FUNDS_ERROR";

    public InsufficientFundsException(String message) {
        super(ERROR_CODE, message, HttpStatus.BAD_REQUEST);
    }
}
