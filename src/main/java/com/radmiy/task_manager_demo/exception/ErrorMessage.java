package com.radmiy.task_manager_demo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    IS_NULL(HttpStatus.BAD_REQUEST, "Task is empty"),
    NULL_ID(HttpStatus.BAD_REQUEST, "ID cannot be null"),
    NEGATIVE_ID(HttpStatus.BAD_REQUEST, "ID cannot be 0 or negative"),


    TASK_NOT_EXIST(HttpStatus.NOT_FOUND, "Task id=%s does not exist");

    private final HttpStatus status;
    private final String message;
}
