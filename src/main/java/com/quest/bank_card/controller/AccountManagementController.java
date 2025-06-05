package com.quest.bank_card.controller;

import com.quest.bank_card.dto.CardCreateDto;
import com.quest.bank_card.dto.CardResponseDto;
import com.quest.bank_card.dto.MoneyDto;
import com.quest.bank_card.dto.UserResponseDto;
import com.quest.bank_card.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(name = "Accounts", description = "Операции для управления учетными записями пользователей")
@RequestMapping("api/v1/account")
public interface AccountManagementController {

    @Operation(summary = "Создать карту пользователя", description = "Запрос на создание карты пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карта успешно создана",
                    content = @Content(schema = @Schema(implementation = CardResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Случилась ошибка при создании карты")
    })
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{userId}/card",
            produces = { "application/json;charset=UTF-8" },
            consumes = { "application/json;charset=UTF-8" }
    )
    ResponseEntity<?> createCard(@PathVariable UUID userId, @RequestBody CardCreateDto cardCreateDto);

    @Operation(summary = "Обновить карту пользователя", description = "Запрос на обновление карты пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карта успешно обновлена",
                    content = @Content(schema = @Schema(implementation = CardResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Случилась ошибка при обновлении карты")
    })
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/{userId}/card/{cardId}",
            produces = { "application/json;charset=UTF-8" },
            consumes = { "application/json;charset=UTF-8" }
    )
    ResponseEntity<?> updateCard(@PathVariable("userId") UUID userId, @PathVariable("cardId") UUID cardId, @RequestBody CardCreateDto cardCreateDto);

    @Operation(summary = "Удалить карту пользователя", description = "Удаляет карту пользователя по ID карты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карта удалена"),
    })
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{userId}/card/{cardId}"
    )
    ResponseEntity<String> deleteCardById(@PathVariable("userId") UUID userId, @PathVariable("cardId") UUID cardId);

    @Operation(summary = "Удалить карты пользователя", description = "Удаляет все карты пользователя по списку ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карты удалены"),
    })
    @RequestMapping(
            method = RequestMethod.DELETE,
            consumes = { "application/json" },
            value = "/{userId}/cards"

    )
    ResponseEntity<String> deleteCardsByIds(@PathVariable UUID userId, @RequestBody List<UUID> ids);

    @Operation(summary = "Удалить все карты привязанные к пользователю", description = "Удаляет все карты пользователя, владельцем которых он является")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карты удалены"),
    })
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{userId}/cards/full"

    )
    ResponseEntity<String> deleteCardsByUserId(@PathVariable UUID userId);

    @Operation(summary = "Получить все карты привязанные к пользователю", description = "Возвращает все карты пользователя, владельцем которых он является")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карты успешно отображаются",
            content = @Content(schema = @Schema(implementation = CardResponseDto.class)))
    })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{userId}/cards"

    )
    ResponseEntity<?> getCardsByUserId(@PathVariable UUID userId, @AuthenticationPrincipal CustomUserDetails customUserDetails);

    @Operation(summary = "Получить все карты привязанные к пользователю c учетом фильтрации и пагинации", description = "Возвращает все карты пользователя, владельцем которых он является, плюс фильтрация и пагинация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карты успешно отображаются",
                    content = @Content(schema = @Schema(implementation = CardResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Указаны невалидные параметры для фильтрации")
    })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{userId}/cards/filter"

    )
    ResponseEntity<?> getCardsByUserIdAndCriteria(@PathVariable UUID userId,
                                                  @RequestParam(defaultValue = "0")
                                                  int page,

                                                  @RequestParam(defaultValue = "20")
                                                  int size,

                                                  @RequestParam(required = false) String status,

                                                  @RequestParam(defaultValue = "dateCreate") String sortBy,
                                                  @RequestParam(defaultValue = "desc") String sortDirection,

                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,

                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails);

    @Operation(summary = "Получить балансе по карте", description = "Возвращает баланс по карте пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Баланс успешно отображается",
                    content = @Content(schema = @Schema(implementation = MoneyDto.class))),
            @ApiResponse(responseCode = "404", description = "Карта не найдена")
    })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{userId}/card/{cardId}/balance"

    )
    ResponseEntity<?> getBalance(@PathVariable UUID userId, @PathVariable UUID cardId, @AuthenticationPrincipal CustomUserDetails customUserDetails);

    @Operation(summary = "Получить конкретную карту по ID карты и пользователя", description = "Возвращает конкретную карту по ID карты и ID пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карта успешно отображаются",
                    content = @Content(schema = @Schema(implementation = CardResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Карта не найдена")
    })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{userId}/card/{cardId}"

    )
    ResponseEntity<?> getCardByUserId(@PathVariable UUID userId, @PathVariable UUID cardId, @AuthenticationPrincipal CustomUserDetails customUserDetails);

    @Operation(summary = "Запрос на блокировку карты", description = "Запрос на блокировку карты по ID карты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карта успешно заблокирована"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена. Карта была уже заблокирована")
    })
    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/{userId}/block/{cardId}"

    )
    ResponseEntity<?> requestBlockCard(@PathVariable UUID userId, @PathVariable UUID cardId, @AuthenticationPrincipal CustomUserDetails customUserDetails);
}
