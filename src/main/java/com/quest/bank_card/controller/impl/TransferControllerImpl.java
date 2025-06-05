package com.quest.bank_card.controller.impl;

import com.quest.bank_card.controller.TransferController;
import com.quest.bank_card.dto.TransferDto;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.exception.ExpiredStatusCardException;
import com.quest.bank_card.service.TransferService;
import com.quest.bank_card.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TransferControllerImpl implements TransferController {

    private final TransferService transferService;
    private final ValidationService validationService;

    @Override
    public ResponseEntity<?> transfer(UUID userId, TransferDto transferDto) {
        Money money = validationService.validateAmount(transferDto.getAmount());
        try {
            transferService.transferBetweenUserCards(transferDto.getFromCardId(),
                    transferDto.getToCardId(), userId, money);
        } catch (ExpiredStatusCardException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok("Transfer completed successfully");
    }
}
