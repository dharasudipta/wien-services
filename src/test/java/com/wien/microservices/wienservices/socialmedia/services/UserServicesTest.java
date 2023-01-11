package com.wien.microservices.wienservices.socialmedia.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wien.microservices.wienservices.socialmedia.beans.User;
import com.wien.microservices.wienservices.socialmedia.entities.UserEntity;
import com.wien.microservices.wienservices.socialmedia.exception.UserNotFoundException;
import com.wien.microservices.wienservices.socialmedia.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServicesTest {
    @InjectMocks
    private UserServices userServices;
    @Mock
    private UserRepository userRepo;

    @Test
    public void FindAllUsers_FoundUsers_OK() {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(1001);
        userEntity1.setName("Sudipta");
        userEntity1.setDob(LocalDate.parse("2023-01-10"));
        when(userRepo.findAll()).thenReturn(List.of(userEntity1));

        try {
            assertEquals(mapUserEntityToBean(userEntity1), userServices.findAllUsers().get(0));
        } catch (IllegalArgumentException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void FindAllUsers_NoUsersFoundWithException_ThrowsException() {
        when(userRepo.findAll()).thenThrow(new UserNotFoundException("No users found."));
        assertThrows(UserNotFoundException.class, () -> {
            userServices.findAllUsers();
        });
    }

    @Test
    public void FindAllUsers_NoUsersFound_ThrowsException() {
        when(userRepo.findAll()).thenReturn(Collections.emptyList());
        assertThrows(UserNotFoundException.class, () -> {
            userServices.findAllUsers();
        });
    }


    private UserEntity mapUserBeanToEntity(User user) {
        UserEntity userEntity = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            userEntity = objectMapper.convertValue(user, UserEntity.class);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Bean to Entity conversion with objectmapper failed.");
        }
        return userEntity;
    }

    private User mapUserEntityToBean(UserEntity userEntity) {
        User newUser = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            newUser = objectMapper.convertValue(userEntity, User.class);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Bean to Entity conversion with objectmapper failed.");
        }
        return newUser;
    }
}
