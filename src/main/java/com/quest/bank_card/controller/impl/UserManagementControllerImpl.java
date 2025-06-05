package com.quest.bank_card.controller.impl;

import com.quest.bank_card.controller.UserManagementController;
import com.quest.bank_card.dto.UserResponseDto;
import com.quest.bank_card.service.UserManagementService;
import com.quest.bank_card.service.UserMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class UserManagementControllerImpl implements UserManagementController {

    private final UserManagementService userManagementService;
    private final UserMapperService userMapperService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUsersByIds(List<UUID> ids) {
        userManagementService.deleteUsersByIds(ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUserById(UUID id) {
        userManagementService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userMapperService.toDtoList(userManagementService.findAllUsers()));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(UUID id) {
        UserResponseDto userResponseDto;
        userResponseDto = userMapperService.toDto(userManagementService.findUserById(id));
        return ResponseEntity.ok(userResponseDto);
    }

}
