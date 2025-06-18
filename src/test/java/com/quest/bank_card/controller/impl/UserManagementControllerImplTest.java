package com.quest.bank_card.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.bank_card.dto.UserResponseDto;
import com.quest.bank_card.entity.User;
import com.quest.bank_card.exception.handler.GlobalExceptionHandler;
import com.quest.bank_card.service.UserManagementService;
import com.quest.bank_card.service.UserMapperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserManagementControllerImplTest {

    private MockMvc mockMvc;
    @Mock
    private UserManagementService userManagementService;
    @Mock
    private UserMapperService userMapperService;
    @InjectMocks
    private UserManagementControllerImpl userManagementController;
    private ObjectMapper objectMapper;
    private UUID testUserId1;
    private UUID testUserId2;
    private UserResponseDto testUserDto1;
    private UserResponseDto testUserDto2;
    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userManagementController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        testUserId1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        testUserId2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");

        testUser1 = User.builder()
                .id(testUserId1)
                .login("testlogin1")
                .name("testuser1")
                .dateCreate(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();

        testUser2 = User.builder()
                .id(testUserId2)
                .login("testlogin2")
                .name("testuser2")
                .dateCreate(LocalDateTime.of(2025, 1, 2, 12, 0))
                .build();

        testUserDto1 = UserResponseDto.builder()
                .id(testUserId1)
                .login("testlogin1")
                .name("testuser1")
                .dateCreate(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();

        testUserDto2 = UserResponseDto.builder()
                .id(testUserId2)
                .login("testlogin2")
                .name("testuser2")
                .dateCreate(LocalDateTime.of(2025, 1, 2, 12, 0))
                .build();
    }

    @Test
    void deleteUsersByIdsTest() throws Exception {
        List<UUID> userIds = Arrays.asList(testUserId1, testUserId2);
        doNothing().when(userManagementService).deleteUsersByIds(userIds);

        mockMvc.perform(delete("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userIds)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(userManagementService, times(1)).deleteUsersByIds(userIds);
    }

    @Test
    void deleteUsersByIdsTestN_withoutContentType() throws Exception {
        List<UUID> userIds = Collections.singletonList(testUserId1);

        mockMvc.perform(delete("/api/v1/users")
                        .content(objectMapper.writeValueAsString(userIds)))
                .andExpect(status().isUnsupportedMediaType());
        verify(userManagementService, never()).deleteUsersByIds(anyList());
    }

    @Test
    void deleteUserByIdTest() throws Exception {
        doNothing().when(userManagementService).deleteUserById(testUserId1);

        mockMvc.perform(delete("/api/v1/users/{id}", testUserId1))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(userManagementService, times(1)).deleteUserById(testUserId1);
    }

    @Test
    void deleteUserByIdTestN_WithInvalidId() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", "invalid-uuid"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; Invalid UUID string: invalid-uuid"));

        verify(userManagementService, never()).deleteUserById(any(UUID.class));
    }

    @Test
    void getAllUsersTest() throws Exception {
        List<User> users = Arrays.asList(testUser1, testUser2);
        List<UserResponseDto> userDtos = Arrays.asList(testUserDto1, testUserDto2);

        when(userManagementService.findAllUsers()).thenReturn(users);
        when(userMapperService.toDtoList(users)).thenReturn(userDtos);

        mockMvc.perform(get("/api/v1/users")
                        .accept("application/json;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(testUserId1.toString()))
                .andExpect(jsonPath("$[0].login").value("testlogin1"))
                .andExpect(jsonPath("$[0].name").value("testuser1"));

        verify(userManagementService, times(1)).findAllUsers();
        verify(userMapperService, times(1)).toDtoList(users);
    }

    @Test
    void getUserByIdTest() throws Exception {
        when(userManagementService.findUserById(testUserId1)).thenReturn(testUser1);
        when(userMapperService.toDto(testUser1)).thenReturn(testUserDto1);

        mockMvc.perform(get("/api/v1/users/{id}", testUserId1)
                        .accept("application/json;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(testUserId1.toString()))
                .andExpect(jsonPath("$.login").value("testlogin1"))
                .andExpect(jsonPath("$.name").value("testuser1"))
                .andExpect(jsonPath("$.dateCreate").exists());

        verify(userManagementService, times(1)).findUserById(testUserId1);
        verify(userMapperService, times(1)).toDto(testUser1);
    }

    @Test
    void getUserByIdTestN_invalidId() throws Exception {
        mockMvc.perform(get("/api/v1/users/{id}", "invalid-uuid")
                        .accept("application/json;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; Invalid UUID string: invalid-uuid"));

        verify(userManagementService, never()).findUserById(any(UUID.class));
        verify(userMapperService, never()).toDto(any(User.class));
    }
}