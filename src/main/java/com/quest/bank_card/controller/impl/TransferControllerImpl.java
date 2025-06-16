package com.quest.bank_card.controller.impl;

import com.quest.bank_card.controller.TransferController;
import com.quest.bank_card.dto.TransferDto;
import com.quest.bank_card.service.CardManagementService;
import com.quest.bank_card.service.MoneyMapperService;
import com.quest.bank_card.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TransferControllerImpl implements TransferController {

    private final TransferService transferService;
    private final CardManagementService cardManagementService;
    private final MoneyMapperService moneyMapperService;

    @Override
    public ResponseEntity<?> transfer(UUID userId, TransferDto transferDto) {
        cardManagementService.validateAndUpdateExpiredCard(transferDto.getFromCardId());
        cardManagementService.validateAndUpdateExpiredCard(transferDto.getToCardId());
        transferService.transferBetweenUserCards(transferDto.getFromCardId(), transferDto.getToCardId(), userId,
                moneyMapperService.toMoney(transferDto.getAmount()));
        return ResponseEntity.ok("Transfer completed successfully");
    }
}
