package com.quest.bank_card.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Schema(description = "Данные для генерации jwt для зарегистрированного пользователя")
public class RequestJwtDto {


    @Schema(description = "Логин пользователя", example = "ivan_petrov", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Owner information must not be blank")
    @Size(min = 2, max = 50, message = "Owner information must be between at 2 or 50 characters long")
    private String login;

    @Schema(description = "Пароль пользователя", example = "ivan_petrov", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(description = "Имя пользователя", example = "Иван Петров", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Owner information must not be blank")
    @Size(min = 2, max = 50, message = "Owner information must be between at 2 or 50 characters long")
    private String name;

}
