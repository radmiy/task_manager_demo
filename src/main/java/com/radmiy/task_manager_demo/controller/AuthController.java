package com.radmiy.task_manager_demo.controller;

import com.radmiy.task_manager_demo.dto.UserAuthDto;
import com.radmiy.task_manager_demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> create(@Valid @RequestBody UserAuthDto dto) {
        log.debug("Register user: {}", dto);
        userService.register(dto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserAuthDto UserAuthDto) {
        return ResponseEntity.ok(userService.generateToken(UserAuthDto));
    }
}
