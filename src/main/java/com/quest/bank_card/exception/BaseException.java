package com.quest.bank_card.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class BaseException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Map<String, Object> details;

    public BaseException(String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = new HashMap<>();
    }

    public BaseException(String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = "BASE_ERROR";
        this.httpStatus = httpStatus;
        this.details = new HashMap<>();
    }

    public BaseException addDetail(String key, Object value) {
        this.details.put(key, value);
        return this;
    }
}
