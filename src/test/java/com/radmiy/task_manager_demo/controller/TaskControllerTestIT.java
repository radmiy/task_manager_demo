package com.radmiy.task_manager_demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radmiy.task_manager_demo.dto.TaskResponseDto;
import com.radmiy.task_manager_demo.repository.model.User;
import com.radmiy.task_manager_demo.repository.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails("admin")
    void createTaskTest() throws Exception {
        //given
        String task = """
                {
                  "id": "",
                  "title": "Test",
                  "description": "Test",
                  "status": "TODO",
                  "priority": "LOW",
                  "author": "",
                  "assignee": "",
                  "createdAt": "",
                  "updatedAt": ""
                }
                """;

        //when
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(task))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    TaskResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), TaskResponseDto.class);
                    assertNotNull(actual.getId());
                    assertEquals("Test", actual.getTitle());
                    assertEquals("admin", actual.getAuthor().getUsername());
                    assertNotNull(actual.getCreatedAt());
                    assertNotNull(actual.getUpdatedAt());
                });
    }

    @Test
    @WithUserDetails("admin")
    void getTaskById() throws Exception {
        mockMvc.perform(get("/api/tasks/550e8400-e29b-41d4-a716-446655440001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("admin")
    void getTaskByInvalidId() throws Exception {
        mockMvc.perform(get("/api/tasks/550e8400-e29b-41d4-a716-446655440000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("admin")
    void updateTaskTest() throws Exception {
        String task = """
                {
                  "id": "",
                  "title": "Test1",
                  "description": "Test1",
                  "status": "TODO",
                  "priority": "LOW",
                  "author": "",
                  "assignee": "",
                  "createdAt": "",
                  "updatedAt": ""
                }
                """;

        //when
        mockMvc.perform(put("/api/tasks/550e8400-e29b-41d4-a716-446655440001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(task))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    TaskResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), TaskResponseDto.class);
                    assertNotNull(actual.getId());
                    assertEquals("Test1", actual.getTitle());
                    assertEquals("admin", actual.getAuthor().getUsername());
                    assertNotNull(actual.getCreatedAt());
                    assertNotNull(actual.getUpdatedAt());
                });
    }

    @Test
    void updateShouldThrowExceptionWhenDifferentUserUpdateTest() throws Exception {
        //given
        User radmiyUser = User.builder()
                .id(UUID.randomUUID())
                .username("radmiy")
                .role(UserRole.USER)
                .build();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                radmiyUser, null, radmiyUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String task = """
                {
                  "id": "",
                  "title": "Test1",
                  "description": "Test1",
                  "status": "TODO",
                  "priority": "LOW",
                  "author": "",
                  "assignee": "",
                  "createdAt": "",
                  "updatedAt": ""
                }
                """;

        //when
        mockMvc.perform(put("/api/tasks/550e8400-e29b-41d4-a716-446655440001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(task))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    void deleteTaskTest() throws Exception {
        mockMvc.perform(delete("/api/tasks/550e8400-e29b-41d4-a716-446655440001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails("admin")
    void getTasks() throws Exception {
        //given
        String task = """
                {
                  "id": "",
                  "title": "Test",
                  "description": "Test",
                  "status": "TODO",
                  "priority": "LOW",
                  "author": "",
                  "assignee": "",
                  "createdAt": "",
                  "updatedAt": ""
                }
                """;

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(task));

        //when
        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "550e8400-e29b-41d4-a716-446655440001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("Настроить окружение", "Test")));
    }
}