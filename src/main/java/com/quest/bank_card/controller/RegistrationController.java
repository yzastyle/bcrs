package com.quest.bank_card.controller;

import com.quest.bank_card.dto.RegistrationDto;
import com.quest.bank_card.entity.User;
import com.quest.bank_card.service.UserManagementService;
import com.quest.bank_card.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final ValidationService validationService;
    private final UserManagementService userManagementService;

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegistrationDto form) {
        User user = validationService.validateUser(form);
        if (userManagementService.isFirstUser()) {
            user.setRole("ADMIN");
        } else {
            user.setRole("USER");
        }
        userManagementService.saveUser(user);
        return "success";
    }
}
