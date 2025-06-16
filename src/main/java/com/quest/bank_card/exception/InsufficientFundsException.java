package com.quest.bank_card.exception;

public class InsufficientFundsException extends BaseException {

    public static final String ERROR_CODE = "INSUFFICIENT_FUNDS_ERROR";

    public InsufficientFundsException(String message) {
        super(ERROR_CODE, message);
    }
}
