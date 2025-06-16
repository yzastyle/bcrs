package com.quest.bank_card.exception;

import java.util.UUID;

public class UnauthorizedException extends BaseException {

    public static final String ERROR_CODE = "AUTHORIZATION_ERROR";

    public UnauthorizedException(UUID userId) {
        super(ERROR_CODE, "Authentication error. You are not the user with id=" + userId);
    }
    public UnauthorizedException(String message) {
        super(ERROR_CODE, message);
    }
}
