package com.wien.microservices.wienservices.socialmedia.repository;

import com.wien.microservices.wienservices.socialmedia.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {
}
