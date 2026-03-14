package com.radmiy.task_manager_demo.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
@Setter
public class ServiceException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public ServiceException(ErrorMessage error) {
        this(error.getStatus(), error.getMessage());
        this.status = error.getStatus();
        this.message = error.getMessage();
    }

    public ServiceException(ErrorMessage error, UUID value) {
        this(error.getStatus(), error.getMessage());
        this.message = getMessage(value.toString());
    }

    public ServiceException(ErrorMessage error,  String... value) {
        this(error.getStatus(), error.getMessage());
        this.message = getMessage(value);
    }

    public ServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    private String getMessage(String... value) {
        return String.format(this.message, value);
    }
}
