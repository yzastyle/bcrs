package com.quest.bank_card.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Данные для генерации jwt для зарегистрированного пользователя")
public class RequestJwtDto {

    @Schema(description = "Логин пользователя", example = "ivan_petrov", requiredMode = Schema.RequiredMode.REQUIRED)
    private String login;

    @Schema(description = "Пароль пользователя", example = "ivan_petrov", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(description = "Имя пользователя", example = "Иван Петров", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

}
