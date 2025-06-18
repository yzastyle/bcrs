package com.quest.bank_card.repository;

import com.quest.bank_card.entity.Card;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID>, JpaSpecificationExecutor<Card> {

    List<Card> findByUserId(UUID id);

    boolean existsByIdAndUserId(UUID cardId, UUID userId);

    boolean existsByNumber(String number);

    @Query("SELECT c FROM Card c JOIN FETCH c.user")
    List<Card> findAllWithUsers();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("Select c From Card c where c.id = :id")
    Optional<Card> findByIdWithLock(@Param("id") UUID id);
}
