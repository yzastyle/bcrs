package com.quest.bank_card.exception;

import java.util.UUID;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(UUID userId) {
        super("User ID=" + userId + " does not belong to you");
    }
    public UnauthorizedException(String message) {
        super(message);
    }
}
