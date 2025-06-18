package com.quest.bank_card.repository.specification;

import com.quest.bank_card.entity.Card;
import com.quest.bank_card.model.Status;
import com.quest.bank_card.dto.CardSearchCriteriaDto;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Utility class for building dynamic JPA Specification queries for Card entities.
 * <p>
 * All methods handle null input gracefully by returning {@code conjunction()}
 * (always-true predicate) to avoid filtering when criteria is not specified.
 */
public class CardSpecifications {

    public static Specification<Card> belongsToUser(UUID userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Card> hasExpirationDateLike(String expirationDate) {
        return (root, query, criteriaBuilder) -> {
            if (expirationDate == null || expirationDate.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("expirationDate"), expirationDate);
        };
    }

    public static Specification<Card> hasStatus(Status status) {
        return ((root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("status"), status));
    }

    public static Specification<Card> createdAfter(LocalDateTime localDateTime) {
        return (root, query, criteriaBuilder) ->
                localDateTime == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.greaterThanOrEqualTo(root.get("dateCreate"), localDateTime);
    }

    public static Specification<Card> createdBefore(LocalDateTime localDateTime) {
        return (root, query, criteriaBuilder) ->
                localDateTime == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.lessThanOrEqualTo(root.get("dateCreate"), localDateTime);

    }

    /**
     * Combines all search criteria into a single Specification with user ownership constraint.
     * All null criteria are ignored, resulting in no filtering for those fields.
     */
    public static Specification<Card> withCriteria(UUID userId, CardSearchCriteriaDto cardSearchCriteria) {
        return belongsToUser(userId)
                .and(hasStatus(cardSearchCriteria.getStatus()))
                .and(createdAfter(cardSearchCriteria.getCreatedAfter()))
                .and(createdBefore(cardSearchCriteria.getCreatedBefore()))
                .and(hasExpirationDateLike(cardSearchCriteria.getExpirationDate()));
    }
}
