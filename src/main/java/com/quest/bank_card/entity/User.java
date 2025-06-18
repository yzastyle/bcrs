package com.quest.bank_card.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Builder
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String login;
    private String name;
    private String password;
    private String role;
    private LocalDateTime dateCreate;

    @OneToMany(mappedBy = "user", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private List<Card> cards;

    public User(String login, String name, String password) {
        this.login = login;
        this.name = name;
        this.password = password;
        this.dateCreate = LocalDateTime.now();
    }

    public void assignRole(String role) {
        this.role = role;
    }
}
