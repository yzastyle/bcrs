package com.quest.bank_card.controller;

import com.quest.bank_card.dto.RegistrationDto;
import com.quest.bank_card.entity.User;
import com.quest.bank_card.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserManagementService userManagementService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegistrationDto form) {
        User user = new User(form.getLogin(), form.getName(),
                passwordEncoder.encode(form.getPassword()));
        if (userManagementService.isFirstUser()) {
            user.assignRole("ADMIN");
        } else {
            user.assignRole("USER");
        }
        userManagementService.saveUser(user);
        return "success";
    }
}
