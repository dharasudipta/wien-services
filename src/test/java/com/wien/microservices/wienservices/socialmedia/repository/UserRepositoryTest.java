package com.wien.microservices.wienservices.socialmedia.repository;

import com.wien.microservices.wienservices.socialmedia.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    public void FindAll_FoundUsers_ReturnUsers() {
        List<UserEntity> allUsers = userRepo.findAll();
        assertNotNull(allUsers);
        assertTrue(allUsers.size() > 0);
    }
}
