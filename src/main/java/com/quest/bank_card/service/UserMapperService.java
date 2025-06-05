package com.quest.bank_card.service;

import com.quest.bank_card.dto.UserResponseDto;
import com.quest.bank_card.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMapperService {

    public List<UserResponseDto> toDtoList(List<User> users) {
        return users.stream().map(this::toDto).toList();
    }

    public UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .dateCreate(user.getDateCreate())
                .build();
    }
}
