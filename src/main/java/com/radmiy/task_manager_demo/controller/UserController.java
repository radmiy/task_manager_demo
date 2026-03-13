package com.radmiy.task_manager_demo.controller;

import com.radmiy.task_manager_demo.dto.TaskDto;
import com.radmiy.task_manager_demo.service.TaskService;
import com.radmiy.task_manager_demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto dto) {
        log.debug("Create Task: {}", dto);
        return ResponseEntity.ok().body(userService.create(dto));
    }
}
