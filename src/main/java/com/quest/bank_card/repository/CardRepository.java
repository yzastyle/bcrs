package com.quest.bank_card.repository;

import com.quest.bank_card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID>, JpaSpecificationExecutor<Card> {

    List<Card> findByUserId(UUID id);

    boolean existsByIdAndUserId(UUID cardId, UUID userId);

    boolean existsByNumber(String number);
}
