package com.wien.microservices.wienservices.socialmedia.repository;

import com.wien.microservices.wienservices.socialmedia.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
