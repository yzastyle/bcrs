package com.quest.bank_card.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("User with id=" + id + " not found");
    }
    public UserNotFoundException(String login) {
        super("User with login=" + login + " not found");
    }
}
