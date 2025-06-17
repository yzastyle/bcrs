package com.quest.bank_card.controller;

import com.quest.bank_card.dto.RequestJwtDto;
import com.quest.bank_card.dto.TokenResponseDto;
import com.quest.bank_card.entity.User;
import com.quest.bank_card.exception.ValidationException;
import com.quest.bank_card.security.JwtService;
import com.quest.bank_card.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class JwtController {

    private final JwtService jwtService;
    private final UserManagementService userManagementService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/jwt")
    public ResponseEntity<?> jwt(@RequestBody RequestJwtDto requestJwtDto) {
        User user = userManagementService.findByLoginUser(requestJwtDto.getLogin());
        if (!user.getName().equals(requestJwtDto.getName())) {
            throw new ValidationException("The specified name=" +
                        requestJwtDto.getName() + "does not belong to the user");
        }
        if (!passwordEncoder.matches(requestJwtDto.getPassword(), user.getPassword())) {
            throw new ValidationException("Incorrect password");
        }
        return ResponseEntity.ok(new TokenResponseDto(jwtService.generateJwtToken(user)));
    }
}
