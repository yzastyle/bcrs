package com.quest.bank_card.service;

import com.quest.bank_card.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserManagementService {

    User saveUser(User user);

    void deleteUserById(UUID id);

    void deleteUsersByIds(List<UUID> ids);

    User findUserById(UUID id);

    List<User> findAllUsers();

    boolean isExists(String login);

    boolean isFirstUser();

    User findByLoginUser(String login);
}
