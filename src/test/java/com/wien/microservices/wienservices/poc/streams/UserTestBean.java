package com.wien.microservices.wienservices.poc.streams;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wien.microservices.wienservices.socialmedia.beans.Post;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonFilter("UserDOBFilter")
public class UserTestBean {
    private int id;

    @Size(min = 2, message = "Name length must be greater than two.")
    private String name;
    @NotNull
    @Past(message = "Date of birth must be in past.")
    private LocalDate dob;

    private List<Post> posts;

//    public User() {
//    }
//
//    public User(int id, String name, LocalDate dob, List<Post> posts) {
//        this.id = id;
//        this.name = name;
//        this.dob = dob;
//        this.posts = posts;
//    }


    public UserTestBean(int id, String name, LocalDate dob) {
        this.id = id;
        this.name = name;
        this.dob = dob;
    }

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

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTestBean user = (UserTestBean) o;
        return id == user.id && name.equals(user.name) && dob.equals(user.dob);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dob);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", posts=" + posts +
                '}';
    }
}
