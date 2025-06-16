package com.quest.bank_card.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final String errorCode;

    public BaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
