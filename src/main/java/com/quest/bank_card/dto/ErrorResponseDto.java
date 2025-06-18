package com.quest.bank_card.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@Schema(description = "Информация по ошибке")
public class ErrorResponseDto {
    @Schema(description = "Идентификатор ошибки", example = "046ed56c-dc23-4d01-aafb-8ef44db7f6c2", requiredMode = Schema.RequiredMode.REQUIRED)
    private final UUID id;

    @Schema(description = "Код ошибки", example = "UNAUTHORIZED_ERROR", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String errorCode;

    @Schema(description = "Описание ошибки", example = "User login must be not null", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String message;

    private final LocalDateTime timestamp;

    @Schema(description = "Подробное описание ошибок")
    private final Map<String, Object> details;

    @Schema(description = "Статус код ответа http запроса", example = "404", requiredMode = Schema.RequiredMode.REQUIRED)
    private final int statusCode;
}
