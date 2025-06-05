package com.quest.bank_card.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "Инициалы владельца для создания банковской карты")
public class CardCreateDto {

    @Schema(description = "Владелец карты", example = "IVAN PETROV", requiredMode = Schema.RequiredMode.REQUIRED)
    private String owner;
}
