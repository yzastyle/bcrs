package com.quest.bank_card.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends BaseException {

    public static final String ERROR_CODE = "VALIDATION_ERROR";

    public ValidationException(String message) {
        super(ERROR_CODE, message, HttpStatus.BAD_REQUEST);
    }
}
