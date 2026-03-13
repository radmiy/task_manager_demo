package com.radmiy.task_manager_demo.controller;

import com.radmiy.task_manager_demo.exception.ExceptionMessageModel;
import com.radmiy.task_manager_demo.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionMessageModel> handleException(ServiceException ex) {
        ExceptionMessageModel errorMessage = new ExceptionMessageModel(ex.getMessage());
        log.error(ex.getMessage());
        return new ResponseEntity<>(errorMessage, ex.getStatus());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionMessageModel> handleException(AuthorizationDeniedException ex) {
        ExceptionMessageModel errorMessage = new ExceptionMessageModel(ex.getMessage());
        log.error(ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessageModel> handleException(Exception ex) {
        ExceptionMessageModel errorMessage = new ExceptionMessageModel(ex.getMessage());
        log.error(ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

}
