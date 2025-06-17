package com.quest.bank_card.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ErrorResponseDto {
    private final UUID id;
    private final String errorCode;
    private final String message;
    private final LocalDateTime timestamp;
    private final Map<String, Object> details;
    private final int statusCode;
}
