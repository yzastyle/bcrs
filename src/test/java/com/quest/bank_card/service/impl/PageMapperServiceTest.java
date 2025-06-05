package com.quest.bank_card.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.bank_card.BankCardApplicationTests;
import com.quest.bank_card.dto.CardResponseDto;
import com.quest.bank_card.dto.CardSearchCriteriaDto;
import com.quest.bank_card.dto.PagedResponseDto;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.service.AccountManagementService;
import com.quest.bank_card.service.CardMapperService;
import com.quest.bank_card.service.PageMapperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PageMapperServiceTest extends BankCardApplicationTests {

    @Autowired
    CardMapperService cardMapperService;
    @Autowired
    PageMapperService pageMapperService;
    @Autowired
    AccountManagementService accountManagementService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void PagedResponseDtoToJsonTest() throws JsonProcessingException {
        Sort sort = Sort.by(Sort.Direction.DESC, "dateCreate");
        Pageable pageable = PageRequest.of(0, 10, sort);

        Page<Card> page = accountManagementService.findCardsByCriteria(UUID.fromString("d17ba058-3684-41cc-9cdb-3ea95d009000"),
                CardSearchCriteriaDto.empty(), pageable);

        Page<CardResponseDto> dtoPage = page.map(cardMapperService::toDto);

        PagedResponseDto<CardResponseDto> responseDto = pageMapperService.toDto(dtoPage, CardSearchCriteriaDto.empty().toFilterMap());

        String jsonPage = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDto);

        JsonNode jsonNode = objectMapper.readTree(jsonPage);

        assertTrue(jsonNode.has("data"));
        assertTrue(jsonNode.has("hasMore"));
        assertTrue(jsonNode.has("currentPage"));
        assertTrue(jsonNode.has("pageSize"));
        assertTrue(jsonNode.has("totalPages"));
        assertTrue(jsonNode.has("totalCount"));
        assertTrue(jsonNode.has("filters"));

        assertEquals(11, jsonNode.get("totalCount").asLong());

        JsonNode dataArray = jsonNode.get("data");

        JsonNode firstCard = dataArray.get(0);
        assertNotNull(firstCard);

        assertTrue(firstCard.has("id"));
        assertTrue(firstCard.has("number"));
        assertTrue(firstCard.has("owner"));
        assertTrue(firstCard.has("expirationDate"));
        assertTrue(firstCard.has("status"));
        assertTrue(firstCard.has("deposit"));
        assertTrue(firstCard.has("userId"));
        assertTrue(firstCard.has("dateCreate"));
    }
}
