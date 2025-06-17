package com.quest.bank_card.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserNotFoundException extends BaseException {

    public static final String ERROR_CODE = "USER_NOT_FOUND_ERROR";

    public UserNotFoundException(UUID id) {
        super(ERROR_CODE, "User with id=" + id + " not found", HttpStatus.NOT_FOUND);
    }
    public UserNotFoundException(String login) {
        super(ERROR_CODE, "User with login=" + login + " not found", HttpStatus.NOT_FOUND);
    }
}
