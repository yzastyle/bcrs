package com.quest.bank_card.dto;

import com.quest.bank_card.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Setter
@Getter
@Schema(description = "Информация о карте")
public class CardResponseDto {

    @Schema(description = "Уникальный идентификатор карты", example = "550e8400-e29b-41d4-a716-446655441234")
    private UUID id;

    @Schema(description = "Номер карты", example = "1234567890123456")
    private String number;

    @Schema(description = "Владелец карты", example = "IVAN PETROV")
    private String owner;

    @Schema(description = "Дата истечения карты в формате MM/YY", example = "12/28")
    private String expirationDate;

    @Schema(description = "Статус карты", example = "active")
    private Status status;

    @Schema(description = "Депозит карты", example = "1000.50")
    private BigDecimal deposit;

    @Schema(description = "Id пользователя владеющего картой", example = "551e2345-e29b-41d4-a716-446655441234")
    private UUID userId;

    @Schema(description = "Дата создания карты", example = "2025-06-03T10:30:00")
    private LocalDateTime dateCreate;
}
