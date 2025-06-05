package com.quest.bank_card.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class PagedResponseDto<T> {

    private List<CardResponseDto> data;

    private boolean hasMore;
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalCount;

    private Map<String, Object> filters;
}
