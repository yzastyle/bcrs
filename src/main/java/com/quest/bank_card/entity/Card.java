package com.quest.bank_card.entity;

import com.quest.bank_card.exception.ValidationException;
import com.quest.bank_card.model.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.quest.bank_card.util.CardUtil.toUpperLatinOwnerName;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cards")
@Getter
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String number;
    private String owner;
    private String expirationDate;

    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    @Embedded
    @Setter
    private Money deposit;

    private LocalDateTime dateCreate;

    public Card(String number, String owner, String expirationDate, Status status, Money deposit) {
        this.number = number;
        this.owner = owner;
        this.expirationDate = expirationDate;
        this.status = status;
        this.deposit = deposit;
        this.dateCreate = LocalDateTime.now();
    }

    public void updateStatus(String status) {
        status = status.toUpperCase();
        Status cardStatus;
        try {
            cardStatus = Status.valueOf(status);
        } catch (ValidationException validationException) {
            throw new ValidationException("Status: " + status + " is not exists");
        }
        if (this.status == cardStatus) throw new ValidationException("Card is already " + cardStatus);

        this.status = cardStatus;
    }

    public void updateOwner(String owner) {
        owner = toUpperLatinOwnerName(owner);
        this.owner = owner;
    }
}
