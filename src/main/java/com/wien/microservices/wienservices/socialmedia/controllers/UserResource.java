package com.wien.microservices.wienservices.socialmedia.controllers;

import com.wien.microservices.wienservices.socialmedia.beans.User;
import com.wien.microservices.wienservices.socialmedia.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/user-api")
public class UserResource {

    private UserServices userService;

    @Autowired
    public UserResource(UserServices userService) {
        this.userService = userService;
    }

    @GetMapping(value = "users", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_PROTOBUF_VALUE})
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("user/{userId}")
    public EntityModel<User> getUser(@PathVariable Integer userId) {
        EntityModel<User> userEntityModel = EntityModel.of(userService.findUserById(userId));
        WebMvcLinkBuilder webMvcLinkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUsers());
        userEntityModel.add(webMvcLinkBuilder.withRel("all-users"));
        return userEntityModel;
    }

    @GetMapping("user/{userId}/posts")
    public User getUserPosts(@PathVariable Integer userId) {
        return userService.findAllPostsByUserId(userId);
    }

    @GetMapping("user/{userId}/post/{postId}")
    public User getUserPostById(@PathVariable Integer userId, @PathVariable Integer postId) {
        User user = userService.findSPostOfUserByIds(userId, postId);
//        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(user);
//        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "posts");
//        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserDOBFilter", filter);
//        mappingJacksonValue.setFilters(filterProvider);
        return user;
    }

    @PostMapping("user")
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        User savedUser = userService.saveUser(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("user")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        userService.updateUser(user);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("user")
    public ResponseEntity<User> updateUserPartially(@RequestBody User user) {
        userService.patchUpdateUser(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("user/{userId}")
    public ResponseEntity deleteUserById(@PathVariable Integer userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

}
