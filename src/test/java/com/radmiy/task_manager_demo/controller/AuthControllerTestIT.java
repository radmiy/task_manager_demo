package com.radmiy.task_manager_demo.controller;

import com.radmiy.task_manager_demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerShouldReturnCreatedWhenDataIsValidTest() throws Exception {
        //given
        String radmiy = """
                {
                  "username": "radmiy",
                  "password": "radmiy",
                  "email": "radmiy@example.com",
                  "role": "USER"
                }
                """;

        //when
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(radmiy))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));

        assertTrue(userRepository.findByUsername("radmiy").isPresent());
    }

    @Test
    void registerShouldReturnBadRequestWhenEmailIsInvalidTest() throws Exception {
        String radmiy = """
                {
                  "username": "radmiy",
                  "password": "radmiy",
                  "email": "invalid",
                  "role": "USER"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(radmiy))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginShouldReturnTokenWhenCredentialsAreCorrectTest() throws Exception {
        //given
        String admin = """
                {
                  "username": "admin",
                  "password": "admin"
                }
                """;

        //when
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(admin))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String token = result.getResponse().getContentAsString();
                    assertNotNull(token);
                    assertTrue(token.length() > 50);
                });
    }

    @Test
    void loginShouldReturnTokenWhenCredentialsWithEmailAreCorrectTest() throws Exception {
        //given
        String admin = """
                {
                  "email": "admin@example.com",
                  "password": "admin"
                }
                """;

        //when
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(admin))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String token = result.getResponse().getContentAsString();
                    assertNotNull(token);
                    assertTrue(token.length() > 50);
                });
    }

    @Test
    void loginShouldReturnBadRequestWhenCredentialsAreInvalidTest() throws Exception {
        //given
        String admin = """
                {
                  "username": "admin",
                  "password": "admin1"
                }
                """;

        //when
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(admin))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertEquals("{\"message\":\"Bad credentials\"}",
                                result.getResponse().getContentAsString()));
    }
}