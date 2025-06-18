package com.quest.bank_card.service.impl;

import com.quest.bank_card.BankCardApplicationTests;
import com.quest.bank_card.entity.Card;
import com.quest.bank_card.entity.Money;
import com.quest.bank_card.entity.User;
import com.quest.bank_card.exception.UserNotFoundException;
import com.quest.bank_card.exception.ValidationException;
import com.quest.bank_card.model.Status;
import com.quest.bank_card.service.UserManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagementServiceImplTest extends BankCardApplicationTests {

    @Autowired
    UserManagementService userManagementService;

    @Test
    public void createUserTest() {
        User user = new User("login", "name", "test");
        user.assignRole("test");
        user = userManagementService.saveUser(user);

        assertEquals("login", user.getLogin());
        assertEquals("name", user.getName());
    }

    @Test
    public void createUserTestNegative() {
        User user = new User("Sash_3", "name", "test");
        user.assignRole("test");

        assertThrows(ValidationException.class, () -> userManagementService.saveUser(user));
    }

    @Test
    public void deleteUserTest() {
        userManagementService.deleteUserById(UUID.fromString("58c80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a"));

        assertThrows(UserNotFoundException.class,
                () -> userManagementService.findUserById(UUID.fromString("58c80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a")));
    }

    @Test
    public void deleteUsersTest() {
        List<UUID> ids = List.of(UUID.fromString("d17ba058-3684-41cc-9fdb-3ea95d0a9d6f"),
                UUID.fromString("d17ba058-3684-41cc-9cfb-3ea95d0a9d6f"));
        userManagementService.deleteUsersByIds(ids);

        assertThrows(UserNotFoundException.class,
                () -> userManagementService.findUserById(UUID.fromString("d17ba058-3684-41cc-9fdb-3ea95d0a9d6f")));
        assertThrows(UserNotFoundException.class,
                () -> userManagementService.findUserById(UUID.fromString("d17ba058-3684-41cc-9cfb-3ea95d0a9d6f")));
    }

    @Test
    @Transactional
    public void findUserByIdWithCard() {
        User user = userManagementService.findUserById(UUID.fromString("d17ba058-3684-41cc-9cdb-3ea95d0a9d6f"));
        List<Card> cards = user.getCards();

        assertNotNull(cards);
        assertEquals(UUID.fromString("ff8d0496-46d7-4264-8570-df21b68ed5ff"), cards.get(0).getId());
        assertEquals("Alice Johnson", cards.get(0).getOwner());
        assertEquals("4111222233334444", cards.get(0).getNumber());
        assertEquals("12/25", cards.get(0).getExpirationDate());
        assertEquals(Status.ACTIVE, cards.get(0).getStatus());
        assertFalse(cards.get(0).getDeposit().isLessThan(new Money(new BigDecimal("1000.00"))));
    }

    @Test
    public void findAllUsersTest() {
        List<User> users = userManagementService.findAllUsers();
        assertNotNull(users);
        assertNotEquals(0, users.size());
    }

    @Test
    public void findUserByLoginTest() {
        String login = "new bob";
        User user = userManagementService.findUserByLogin(login);

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(login, user.getLogin());
    }

}
