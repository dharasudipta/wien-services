package com.wien.microservices.wienservices.socialmedia.controllers;

import com.wien.microservices.wienservices.socialmedia.beans.User;
import com.wien.microservices.wienservices.socialmedia.exception.UserNotFoundException;
import com.wien.microservices.wienservices.socialmedia.services.UserServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(UserResource.class)
@AutoConfigureMockMvc
public class UserResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServices userService;

//    @BeforeAll
//    public void setUp(){
//        mockMvc = new MockMvc;
//    }

    @Test
    public void FindAllUsers_FoundUsers_OK() throws Exception {

        User user1 = new User();
        user1.setId(1001);
        user1.setName("Sudipta");
        user1.setDob(LocalDate.parse("2023-01-10"));
        when(userService.findAllUsers()).thenReturn(List.of(user1));
        mockMvc.perform(MockMvcRequestBuilders.get("/user-api/users").header("Authorization", "Basic dXNlcjpwYXNz"))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("[{\"id\":1001,\"name\":\"Sudipta\",\"dob\":\"2023-01-10\"}" +
//                                ",{\"id\":1002,\"name\":\"Biswabandhu\",\"dob\":\"2023-01-10\"}" +
//                                ",{\"id\":1003,\"name\":\"Amit\",\"dob\":\"2023-01-10\"}" +
//                                ",{\"id\":1004,\"name\":\"Yagu\",\"dob\":\"2023-01-10\"}" +
//                                ",{\"id\":1005,\"name\":\"Arnab\",\"dob\":\"2023-01-10\"}" +
//                                ",{\"id\":1006,\"name\":\"Rounak\",\"dob\":\"2023-01-10\"}" +
                                "]"))
                .andReturn();

    }

    @Test
    public void FindAllUsers_UserNotFoundException_NOT_FOUND() throws Exception {
        when(userService.findAllUsers()).thenThrow(new UserNotFoundException("No users found"));
        mockMvc.perform(MockMvcRequestBuilders.get("/user-api/users").header("Authorization", "Basic dXNlcjpwYXNz"))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\n" +
                                "    \"message\": \"No users found\",\n" +
                                "    \"details\": \"uri=/user-api/users\"\n" +
                                "}"))
                .andReturn();

    }
}
