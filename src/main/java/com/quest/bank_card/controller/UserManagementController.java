package com.quest.bank_card.controller;


import com.quest.bank_card.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

@Tag(name = "Users", description = "Операции для управления пользователями")
@RequestMapping("api/v1/users")
public interface UserManagementController {

    @Operation(summary = "Удалить пользователей", description = "Удаляет пользователей по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователи удалены"),
    })
    @RequestMapping(
            method = RequestMethod.DELETE,
            consumes = { "application/json" },
            value = ""
    )
    ResponseEntity<String> deleteUsersByIds(@RequestBody List<UUID> ids);

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь удален"),
    })
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}"
    )
    ResponseEntity<String> deleteUserById(@PathVariable UUID id);

    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class)))
    })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "",
            produces = { "application/json;charset=UTF-8" }
    )
    ResponseEntity<?> getAllUsers();

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}",
            produces = { "application/json;charset=UTF-8" }
    )
    ResponseEntity<?> getUserById(@PathVariable UUID id);
}
