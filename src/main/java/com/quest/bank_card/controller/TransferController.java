package com.quest.bank_card.controller;

import com.quest.bank_card.dto.TransferDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@Tag(name = "Transfer", description = "Операция для осуществления переводов между картами")
@RequestMapping("api/v1/transfer")
public interface TransferController {

    @Operation(summary = "Перевод на другую карту", description = "Переводы балансов между своими картами пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Перевод осуществлен успешно"),
            @ApiResponse(responseCode = "400", description = "Случилась ошибка при переводе")
    })
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{userId}",
            consumes = { "application/json;charset=UTF-8" }
    )
    ResponseEntity<?> transfer(@PathVariable UUID userId, @RequestBody @Valid TransferDto transferDto);
}
