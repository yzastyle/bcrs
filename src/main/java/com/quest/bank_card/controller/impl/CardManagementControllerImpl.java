package com.quest.bank_card.controller.impl;

import com.quest.bank_card.controller.CardManagementController;
import com.quest.bank_card.dto.CardResponseDto;
import com.quest.bank_card.service.CardManagementService;
import com.quest.bank_card.service.CardMapperService;
import com.quest.bank_card.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CardManagementControllerImpl implements CardManagementController {

    private final CardManagementService cardManagementService;
    private final CardMapperService cardMapperService;
    private final ValidationService validationService;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<List<CardResponseDto>> getAllCards() {
        return ResponseEntity.ok(cardMapperService.toDtoList(cardManagementService.findAllCards()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<?> getCardById(UUID id) {
        return ResponseEntity.ok(cardMapperService.toDto(cardManagementService.findCardById(id)));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateStatus(UUID id, String status) {
        cardManagementService.UpdateCardStatusById(id, validationService.validateStatus(status));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("The card status was successfully updated");
    }
}
