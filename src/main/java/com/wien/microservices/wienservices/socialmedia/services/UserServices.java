package com.wien.microservices.wienservices.socialmedia.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wien.microservices.wienservices.socialmedia.beans.Post;
import com.wien.microservices.wienservices.socialmedia.beans.User;
import com.wien.microservices.wienservices.socialmedia.entities.PostEntity;
import com.wien.microservices.wienservices.socialmedia.entities.UserEntity;
import com.wien.microservices.wienservices.socialmedia.exception.InputValidationException;
import com.wien.microservices.wienservices.socialmedia.exception.UserNotFoundException;
import com.wien.microservices.wienservices.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserServices {

    private UserRepository userRepo;

    @Autowired
    public UserServices(UserRepository userRepository) {
        this.userRepo = userRepository;
    }

    public List<User> findAllUsers() {
        List<UserEntity> allUsers = userRepo.findAll();
        if (CollectionUtils.isEmpty(allUsers)) {
            throw new UserNotFoundException("No users found");
        }
        return userBeanTransformers(allUsers);
    }

    public User findUserById(Integer userId) throws UserNotFoundException, InputValidationException {
        if (userId <= 0)
            throw new InputValidationException("userId is less than or equals to Zero.");

        Optional<UserEntity> userEntity = userRepo.findById(userId);
        if (userEntity.isEmpty()) throw new UserNotFoundException("userID: " + userId);


        Optional<User> user = userEntity.map(userEntity1 -> {
            User user1 = new User();
            user1.setId(userEntity1.getId());
            user1.setName(userEntity1.getName());
            user1.setDob(userEntity1.getDob());
            return user1;
        });
        return user.get();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public User findAllPostsByUserId(Integer userId) {
        Optional<UserEntity> userEntity = userRepo.findById(userId);
        if (Optional.of(userEntity).isEmpty()) return new User();


        Optional<User> user = userEntity.map(new Function<UserEntity, User>() {
            @Override
            public User apply(UserEntity userEntity) {
                User user = new User();
                user.setId(userEntity.getId());
                user.setName(userEntity.getName());
                user.setDob(userEntity.getDob());
                List<PostEntity> listOfPosts = Optional.of(userEntity.getPosts()).get().orElse(Collections.EMPTY_LIST);
                if (CollectionUtils.isEmpty(listOfPosts)) {
                    return user;
                }

                user.setPosts(listOfPosts.stream().map(postEntity -> {
                    Post post = new Post();
                    post.setId(postEntity.getId());
                    post.setDescription(postEntity.getDescription());
                    UserEntity userEnityFromPostEntity = postEntity.getUser();
                    post.setUserId(userEnityFromPostEntity != null ? userEnityFromPostEntity.getId() : 0);
                    return post;
                }).collect(Collectors.toList()));
                return user;
            }
        });
        return Optional.of(user).get().orElse(new User());
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public User findSPostOfUserByIds(Integer userId, Integer postId) {

        User user = findAllPostsByUserId(userId);
        if (CollectionUtils.isEmpty(user.getPosts())) {
            return user;
        }

        user.setPosts(user.getPosts().stream().filter(post -> post.getId().equals(postId)).collect(Collectors.toList()));
        return user;

    }

    public User saveUser(User user) {
        if (user.getId() != 0) {
            throw new InputValidationException("User id must be empty.");
        }

        return mapUserEntityToBean(userRepo.save(mapUserBeanToEntity(user)));
    }

    public void deleteUserById(Integer userId) {
        userRepo.deleteById(userId);
    }

    public User updateUser(User newUser) {
        User existingUser = this.findUserById(newUser.getId());
        if (existingUser.equals(newUser)) {
            return newUser;
        }
        UserEntity updatedUserEntity = userRepo.save(mapUserBeanToEntity(newUser));

        return mapUserEntityToBean(updatedUserEntity);
    }

    public User patchUpdateUser(User newUser) {
        User existingUser = this.findUserById(newUser.getId());
        if (!CollectionUtils.isEmpty(newUser.getPosts())) {
            throw new InputValidationException("You cannot send posts details for User patch update");
        }
        String newUserName = newUser.getName();
        LocalDate newUserDob = newUser.getDob();
        if (newUserName != null && newUserName != "" && !newUserName.equalsIgnoreCase(existingUser.getName())) {
            existingUser.setName(newUserName);
        }
        if (newUserDob != null && !newUserDob.equals(existingUser.getDob())) {
            existingUser.setDob(newUserDob);
        }
        if (existingUser.equals(newUser)) {
            return newUser;
        }
        return mapUserEntityToBean(userRepo.save(mapUserBeanToEntity(existingUser)));
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
//            user = objectMapper.convertValue(userRepo.saveAndFlush(userEntity), User.class);
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
            newUser = objectMapper.convertValue(userRepo.saveAndFlush(userEntity), User.class);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Bean to Entity conversion with objectmapper failed.");
        }
        return newUser;
    }


    private List<User> userBeanTransformers(List<UserEntity> allUsers) {
        return mapToListOfBean(allUsers);
    }

    private List<User> mapToListOfBean(List<UserEntity> allUsers) {
        return allUsers.stream().map(userEntity -> {
            User user = new User();
            user.setId(userEntity.getId());
            user.setName(userEntity.getName());
            user.setDob(userEntity.getDob());
            return user;
        }).collect(Collectors.toList());
    }

}
