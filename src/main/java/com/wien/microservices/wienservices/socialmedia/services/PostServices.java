package com.wien.microservices.wienservices.socialmedia.services;

import com.wien.microservices.wienservices.socialmedia.beans.Post;
import com.wien.microservices.wienservices.socialmedia.entities.PostEntity;
import com.wien.microservices.wienservices.socialmedia.entities.UserEntity;
import com.wien.microservices.wienservices.socialmedia.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServices {

    private PostRepository postRepository;

    public PostServices(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAllPosts() {
        List<PostEntity> allPosts = postRepository.findAll();
        if (CollectionUtils.isEmpty(allPosts)) {
            return Collections.EMPTY_LIST;
        }
        return allPosts.stream().map(postEntity -> {
            Post post = new Post();
            post.setId(postEntity.getId());
            post.setDescription(postEntity.getDescription());
            UserEntity userEntity = postEntity.getUser();
            post.setUserId(userEntity != null ? userEntity.getId() : 0);
            return post;
        }).collect(Collectors.toList());
    }
}
