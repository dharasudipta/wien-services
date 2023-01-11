package com.wien.microservices.wienservices.socialmedia.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Entity(name = "user_details")
public class UserEntity {

    @Id
    @GeneratedValue
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("dob")
    private LocalDate dob;

    @OneToMany(mappedBy = "user")
    private List<PostEntity> posts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Optional<List<PostEntity>> getPosts() {
        return Optional.ofNullable(posts);
    }

    public void setPosts(List<PostEntity> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", posts=" + posts +
                '}';
    }
}
