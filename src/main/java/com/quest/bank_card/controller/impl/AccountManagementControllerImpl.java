package com.quest.bank_card.controller.impl;

import com.quest.bank_card.controller.AccountManagementController;
import com.quest.bank_card.dto.*;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.exception.UnauthorizedException;
import com.quest.bank_card.model.Status;
import com.quest.bank_card.security.CustomUserDetails;
import com.quest.bank_card.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountManagementControllerImpl implements AccountManagementController {

    private final CardMapperService cardMapperService;
    private final AccountManagementService accountManagementService;
    private final PageMapperService pageMapperService;
    private final CardGenerationService cardGenerationService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCard(UUID userId, CardCreateDto cardCreateDto) {
        Card card = accountManagementService.saveCard(userId, cardGenerationService.generateCard(cardCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(cardMapperService.toDto(card));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCard(UUID userId, UUID cardId, CardCreateDto cardCreateDto) {
        Card card = accountManagementService.updateCard(userId, cardId, cardCreateDto.getOwner());
        return ResponseEntity.ok(cardMapperService.toDto(card));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCardById(UUID userId, UUID cardId) {
        accountManagementService.deleteCardById(userId, cardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCardsByIds(UUID userId, List<UUID> ids) {
        accountManagementService.deleteCardsByIds(userId, ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCardsByUserId(UUID userId) {
        accountManagementService.deleteCardsByUserId(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<?> getCardsByUserId(UUID userId, CustomUserDetails customUserDetails) {
        List<CardResponseDto> dtoList;
        if (isNotAdminRole(customUserDetails) &&
                    !customUserDetails.getId().equals(userId)) throw new UnauthorizedException(userId);
        List<Card> cards = accountManagementService.findCardsByUserId(userId);
        dtoList = cardMapperService.toDtoList(cards);
        return ResponseEntity.ok(dtoList);
    }

    @Override
    public ResponseEntity<?> getCardsByUserIdAndCriteria(UUID userId,
                                                         Pageable pageable,
                                                         Status status,
                                                         LocalDateTime createdAfter,
                                                         LocalDateTime createdBefore,
                                                         CustomUserDetails customUserDetails) {
        PagedResponseDto responseDto;
        if (isNotAdminRole(customUserDetails) &&
                !customUserDetails.getId().equals(userId)) throw new UnauthorizedException(userId);
        CardSearchCriteriaDto criteria = CardSearchCriteriaDto.builder()
                .status(status)
                .createdAfter(createdAfter)
                .createdBefore(createdBefore).build();
        Page<Card> cardPage = accountManagementService.findCardsByCriteria(userId, criteria, pageable);
        Page<CardResponseDto> dtoPage = cardPage.map(cardMapperService::toDto);
        responseDto = pageMapperService.toDto(dtoPage, criteria.toFilterMap());
        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<?> getBalance(UUID userId, UUID cardId, CustomUserDetails customUserDetails) {
        MoneyDto dto;
        if (isNotAdminRole(customUserDetails) &&
                !customUserDetails.getId().equals(userId)) throw new UnauthorizedException(userId);
        Money money = accountManagementService.getDepositByCardId(userId, cardId);
        dto = cardMapperService.toDto(money);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<?> getCardByUserId(UUID userId, UUID cardId, CustomUserDetails customUserDetails) {
        CardResponseDto dto;
        if (isNotAdminRole(customUserDetails) &&
                !customUserDetails.getId().equals(userId)) throw new UnauthorizedException(userId);
        Card card = accountManagementService.findUserCard(userId, cardId);
        dto = cardMapperService.toDto(card);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<?> requestBlockCard(UUID userId, UUID cardId, CustomUserDetails customUserDetails) {
        if (isNotAdminRole(customUserDetails) &&
                !customUserDetails.getId().equals(userId)) throw new UnauthorizedException(userId);
        accountManagementService.blockRequest(userId, cardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private boolean isNotAdminRole(CustomUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .noneMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
