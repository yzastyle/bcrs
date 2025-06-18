package com.quest.bank_card.service.impl;

import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.exception.InsufficientFundsException;
import com.quest.bank_card.exception.UnauthorizedException;
import com.quest.bank_card.exception.ValidationException;
import com.quest.bank_card.model.Status;
import com.quest.bank_card.service.CardManagementService;
import com.quest.bank_card.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final CardManagementService cardManagementService;

    @Override
    @Transactional
    public void transferBetweenUserCards(UUID fromCardId, UUID toCardId, UUID userId, Money amount) {
        Card from = cardManagementService.findCardByIdWithLock(fromCardId);
        Card to = cardManagementService.findCardByIdWithLock(toCardId);

        validateTransfer(from, to, userId);
        validateAvailableDeposit(from, amount);

        Money newFromDeposit = from.getDeposit().subtract(amount);
        Money newToDeposit = to.getDeposit().add(amount);

        from.setDeposit(newFromDeposit);
        to.setDeposit(newToDeposit);

        cardManagementService.saveCard(from);
        cardManagementService.saveCard(to);
    }

    private void validateTransfer(Card from, Card to, UUID userId) {
        validateCardIds(from.getId(), to.getId());
        validateCardsOwner(from.getId(), to.getId(), userId);
        validateCardStatus(from);
        validateCardStatus(to);
    }

    private void validateAvailableDeposit(Card from, Money amount) {
        if (from.getDeposit().isLessThan(amount)) {
            throw new InsufficientFundsException("Insufficient funds. Available: "
                            + from.getDeposit().getAmount()
                            + ", Required: "
                            + amount.getAmount()
            );
        }
    }

    private void validateCardStatus(Card card) {
        if (card.getStatus() != Status.ACTIVE) {
            throw new ValidationException("Card is not active: " + card.getStatus());
        }
    }

    private void validateCardsOwner(UUID fromCardId, UUID toCardId, UUID userId) {
        if (!cardManagementService.isCardOwnedBy(fromCardId, userId) || !cardManagementService.isCardOwnedBy(toCardId, userId)) {
            throw new UnauthorizedException("Cards does not belong to user. Operation cancelled.");
        }
    }

    private void validateCardIds(UUID fromCardId, UUID toCardId) {
        if (fromCardId.equals(toCardId)) {
            throw new ValidationException("Transfer canceled. The same card identifiers are specified");
        }
    }
}
