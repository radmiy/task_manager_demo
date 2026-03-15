package com.radmiy.task_manager_demo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    NULL_ID(HttpStatus.BAD_REQUEST, "ID cannot be null"),

    IS_NULL(HttpStatus.BAD_REQUEST, "Task is empty"),
    TASK_NOT_EXIST(HttpStatus.NOT_FOUND, "Task id=%s does not exist"),
    TASK_IS_NOT_FOR_USER(HttpStatus.NOT_FOUND, "Task id=%s does not belong to user=%s"),

    PASSWORD_NULL(HttpStatus.BAD_REQUEST, "User password cannot be null"),
    USERNAME_NULL(HttpStatus.BAD_REQUEST, "Username cannot be null"),
    TITLE_NULL(HttpStatus.BAD_REQUEST, "Title cannot be null"),
    AUTHOR_NULL(HttpStatus.BAD_REQUEST, "Author cannot be null"),

    USER_EXIST(HttpStatus.BAD_REQUEST, "User with username: %s exists"),
    USERNAME_NOT_EXIST(HttpStatus.NOT_FOUND, "User with username: %s does not exist"),
    EMAIL_NOT_EXIST(HttpStatus.NOT_FOUND, "User with email: %s does not exist"),
    EMAIL_NULL(HttpStatus.BAD_REQUEST, "User email cannot be null"),
    EMAIL_EXIST(HttpStatus.BAD_REQUEST, "User with email: %s exists");



    private final HttpStatus status;
    private final String message;
}
