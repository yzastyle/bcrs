package com.quest.bank_card.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "Инициалы владельца для создания банковской карты")
public class CardCreateDto {

    @Schema(description = "Владелец карты", example = "IVAN PETROV", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Owner information must not be blank")
    @Size(min = 2, max = 50, message = "Owner information must be between at 2 or 50 characters long")
    private String owner;
}
