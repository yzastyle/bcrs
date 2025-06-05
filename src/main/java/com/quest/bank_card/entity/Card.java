package com.quest.bank_card.entity;

import com.quest.bank_card.model.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cards")
@Data
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
    @ToString.Exclude
    private User user;

    @Embedded
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

}
