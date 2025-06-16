package com.quest.bank_card.exception;

public class ValidationException extends BaseException {

    public static final String ERROR_CODE = "VALIDATION_ERROR";

    public ValidationException(String message) {
        super(ERROR_CODE, message);
    }
}
