package com.quest.bank_card.dto;

import com.quest.bank_card.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@Setter
@Schema(description = "Критерии для фильтрации карт")
public class CardSearchCriteriaDto {

    private Status status;
    private String expirationDate;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;

    public static CardSearchCriteriaDto empty() {
        return CardSearchCriteriaDto.builder().build();
    }

    public boolean isEmpty() {
        return status == null && createdAfter == null
                && createdBefore == null
                && expirationDate == null;
    }

    public Map<String, Object> toFilterMap() {
        Map<String, Object> filtersMap = new HashMap<>();
        if (status != null) filtersMap.put("status", status);
        if (createdAfter != null) filtersMap.put("createdAfter", createdAfter);
        if (createdBefore != null) filtersMap.put("createdBefore", createdBefore);
        if (expirationDate != null) filtersMap.put("expirationDate", expirationDate);
        return filtersMap;
    }
}
