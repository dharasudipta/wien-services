package com.wien.microservices.wienservices.socialmedia.integrationtest;

import com.wien.microservices.wienservices.socialmedia.beans.User;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SocialMediaIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void contextLoads() throws JSONException {
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Authorization", "Basic dXNlcjpwYXNz"); //ToDo to use basic auth with headers use testRestTemplate.exchange()
        ResponseEntity<String> responseGetAllUsers = testRestTemplate.withBasicAuth("user", "pass").getForEntity("/user-api/users", String.class);
        assertEquals(HttpStatus.OK, responseGetAllUsers.getStatusCode());
        JSONAssert.assertEquals("[{id:1001},{id:1002},{id:1003},{id:1004},{id:1005},{id:1006},{id:2001},{id:2002},{id:2003},{id:2004},{id:2005},{id:2006}]", responseGetAllUsers.getBody(), false);
    }
}
