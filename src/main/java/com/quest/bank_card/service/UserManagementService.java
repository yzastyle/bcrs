package com.quest.bank_card.service;

import com.quest.bank_card.entity.User;

import java.util.List;
import java.util.UUID;

/**
 * Administrative service for low-level user management operations.
 * <p>
 * Provides direct data access and manipulation capabilities for user entities.
 *
 * @see User
 * @see AccountManagementService
 * @author Devon
 */
public interface UserManagementService {

    User saveUser(User user);

    void deleteUserById(UUID id);

    void deleteUsersByIds(List<UUID> ids);

    User findUserById(UUID id);

    /**
     * Retrieves all users in the system without eager-loaded cards relationships.
     *
     * @return the complete list of all users
     */
    List<User> findAllUsers();

    boolean isExists(String login);

    boolean isFirstUser();

    User findUserByLogin(String login);
}
