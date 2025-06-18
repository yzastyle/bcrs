package com.quest.bank_card.service;

import com.quest.bank_card.dto.CardResponseDto;
import com.quest.bank_card.dto.PagedResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class PageMapperService {

    public PagedResponseDto toDto(Page<CardResponseDto> page, Map<String, Object> filters) {
        return PagedResponseDto.builder()
                .data(page.getContent())
                .hasMore(page.hasNext())
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .filters(filters)
                .build();
    }

    public PagedResponseDto empty() {
        return PagedResponseDto.builder()
                .data(Collections.emptyList())
                .hasMore(false)
                .currentPage(0)
                .pageSize(0)
                .totalPages(0)
                .totalCount(0L)
                .filters(Collections.emptyMap())
                .build();
    }
}
