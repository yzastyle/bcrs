package com.quest.bank_card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDto {
    @NotBlank(message = "Login information must not be blank")
    @Size(min = 2, max = 50, message = "Login information must be between at 2 or 50 characters long")
    private String login;

    @NotBlank(message = "Name information must not be blank")
    @Size(min = 2, max = 50, message = "Name information must be between at 2 or 50 characters long")
    private String name;

    private String password;
}
