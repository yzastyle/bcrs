package com.quest.bank_card.controller.impl;

import com.quest.bank_card.controller.TransferController;
import com.quest.bank_card.dto.TransferDto;
import com.quest.bank_card.exception.ExpiredStatusCardException;
import com.quest.bank_card.service.MoneyMapperService;
import com.quest.bank_card.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TransferControllerImpl implements TransferController {

    private final TransferService transferService;
    private final MoneyMapperService moneyMapperService;

    @Override
    public ResponseEntity<?> transfer(UUID userId, TransferDto transferDto) {
        try {
            transferService.transferBetweenUserCards(transferDto.getFromCardId(),
                    transferDto.getToCardId(), userId, moneyMapperService.toMoney(transferDto.getAmount()));
        } catch (ExpiredStatusCardException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok("Transfer completed successfully");
    }
}
