package com.quest.bank_card.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Setter
@Getter
@Schema(description = "Информация о пользователе")
public class UserResponseDto {

    @Schema(description = "Уникальный идентификатор пользователя", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Логин пользователя", example = "ivan_petrov")
    private String login;

    @Schema(description = "Имя пользователя", example = "Иван Петров")
    private String name;

    @Schema(description = "Дата создания пользователя", example = "2025-06-02T10:30:00")
    private LocalDateTime dateCreate;

    @Schema(description = "Список карт")
    private List<CardResponseDto> cards;
}
