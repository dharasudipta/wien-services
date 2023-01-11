package com.wien.microservices.wienservices.socialmedia.controllers;

import com.wien.microservices.wienservices.socialmedia.beans.Post;
import com.wien.microservices.wienservices.socialmedia.services.PostServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "post-api")
public class PostResource {

    private PostServices postServices;

    @Autowired
    public PostResource(PostServices postServices) {
        this.postServices = postServices;
    }

    @GetMapping("posts")
    public List<Post> getAllPosts() {
        return postServices.findAllPosts();
    }
}
