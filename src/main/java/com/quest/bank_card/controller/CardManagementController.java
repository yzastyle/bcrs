package com.quest.bank_card.controller;

import com.quest.bank_card.dto.CardResponseDto;
import com.quest.bank_card.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

@Tag(name = "Cards", description = "Операции для управления картами")
@RequestMapping("api/v1/cards")
public interface CardManagementController {

    @Operation(summary = "Получить все карты", description = "Возвращает список всех карт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список карт пользователей",
                    content = @Content(schema = @Schema(implementation = CardResponseDto.class)))
    })
    @RequestMapping(
            method = RequestMethod.GET,
            produces = { "application/json;charset=UTF-8" }
    )
    ResponseEntity<List<CardResponseDto>> getAllCards();

    @Operation(summary = "Получить карту пользователя по ID карты", description = "Возвращает карту по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карта найдена",
                    content = @Content(schema = @Schema(implementation = CardResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Карта не найдена")
    })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}",
            produces = { "application/json;charset=UTF-8" }
    )
    ResponseEntity<?> getCardById(@PathVariable UUID id);

    @Operation(summary = "Обновить статус карты", description = "Блокирует или активирует каты по ID карты пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус карты успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Нельзя обновить статус одним и тем же значением")
    })
    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/{id}/{status}"
    )
    ResponseEntity<String> updateStatus(@PathVariable UUID id, @PathVariable String status);
}
