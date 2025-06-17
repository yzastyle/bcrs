package com.quest.bank_card.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UnauthorizedException extends BaseException {

    public static final String ERROR_CODE = "UNAUTHORIZED_ERROR";

    public UnauthorizedException(UUID userId) {
        super(ERROR_CODE, "Authentication error. You are not the user with id=" + userId, HttpStatus.UNAUTHORIZED);
    }
    public UnauthorizedException(String message) {
        super(ERROR_CODE, message, HttpStatus.UNAUTHORIZED);
    }
}
