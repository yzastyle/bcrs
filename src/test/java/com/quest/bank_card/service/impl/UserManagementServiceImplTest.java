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
    public void createUserTestN_loginIsAlreadyExists() {
        User user = new User("Sash_3", "name", "test");
        user.assignRole("test");

        assertThrows(ValidationException.class, () -> userManagementService.saveUser(user));
    }

    @Test
    public void deleteUserTest() {
        UUID userId = UUID.fromString("58c80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a");
        userManagementService.deleteUserById(userId);

        assertThrows(UserNotFoundException.class, () -> userManagementService.findUserById(userId));
    }

    @Test
    public void deleteUsersTest() {
        UUID userId1 = UUID.fromString("d17ba058-3684-41cc-9fdb-3ea95d0a9d6f");
        UUID userId2 = UUID.fromString("d17ba058-3684-41cc-9cfb-3ea95d0a9d6f");
        userManagementService.deleteUsersByIds(List.of(userId1, userId2));

        assertThrows(UserNotFoundException.class, () -> userManagementService.findUserById(userId1));
        assertThrows(UserNotFoundException.class, () -> userManagementService.findUserById(userId2));
    }

    @Test
    @Transactional
    public void findUserByIdWithCard() {
        User user = userManagementService.findUserById(UUID.fromString("d17ba058-3684-41cc-9cdb-3ea95d0a9d6f"));
        List<Card> cards = user.getCards();

        assertNotNull(cards);
        assertEquals(UUID.fromString("5abbe672-a9b8-4ed7-8cda-6437b063a55e"), cards.get(0).getId());
        assertEquals("transferBetweenUserCardTest_BlockStatus", cards.get(0).getOwner());
        assertEquals("4111222233334444", cards.get(0).getNumber());
        assertEquals("12/25", cards.get(0).getExpirationDate());
        assertEquals(Status.BLOCKED, cards.get(0).getStatus());
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

    @Test
    public void findUserByLoginTestN_userNotFound() {
        assertThrows(UserNotFoundException.class, () -> userManagementService.findUserByLogin("notlogin"));
    }

    @Test
    public void isExistsTestTrue() {
        assertTrue(userManagementService.isExists("Sash_3"));
    }

    @Test
    public void isFirstUserFalse() {
        assertFalse(userManagementService.isFirstUser());
    }
}
