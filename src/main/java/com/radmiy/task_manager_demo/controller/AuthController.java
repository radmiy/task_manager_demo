package com.radmiy.task_manager_demo.controller;

import com.radmiy.task_manager_demo.dto.UserAuthDto;
import com.radmiy.task_manager_demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Operations for user registration and authentication")
public class AuthController {

    private final UserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with provided username, email, password and role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed or user already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<String> create(@Valid @RequestBody UserAuthDto dto) {
        log.debug("Register user: {}", dto);
        userService.register(dto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @Operation(
            summary = "Authenticate user",
            description = "Validates credentials and returns a JWT access token for further authorized requests."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully authenticated, returns JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcxMDUxNTE1MX0...")
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Invalid username or password"),
            @ApiResponse(responseCode = "400", description = "Malformed request body")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserAuthDto userAuthDto) {
        log.debug("LOGIN user: {}", userAuthDto);
        return ResponseEntity.ok(userService.generateToken(userAuthDto));
    }
}
