package com.quest.bank_card.service.impl;

import com.quest.bank_card.entity.User;
import com.quest.bank_card.exception.UserNotFoundException;
import com.quest.bank_card.exception.ValidationException;
import com.quest.bank_card.repository.UserRepository;
import com.quest.bank_card.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public User saveUser(User user) {
        String login = user.getLogin();
        if (isExists(login)) {
            throw new ValidationException("Login=" + login + " already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteUsersByIds(List<UUID> ids) {
        userRepository.deleteAllById(ids);
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserById(UUID id) throws UserNotFoundException {
       return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isExists(String login) {
        return userRepository.existsByLogin(login);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isFirstUser() {
        return userRepository.count() == 0;
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
    }
}
