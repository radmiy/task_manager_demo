package com.radmiy.task_manager_demo.controller;

import com.radmiy.task_manager_demo.exception.ExceptionMessageModel;
import com.radmiy.task_manager_demo.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionMessageModel> handleException(ServiceException ex) {
        ExceptionMessageModel errorMessage = new ExceptionMessageModel(ex.getMessage());
        log.error(ex.getMessage());
        return new ResponseEntity<>(errorMessage, ex.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        log.error("Validation failed: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        if (ex.getCause() instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException ife) {
            if (ife.getTargetType() != null && ife.getTargetType().isEnum()) {
                String fieldName = ife.getPath().get(0).getFieldName();
                String values = Arrays.toString(ife.getTargetType().getEnumConstants());

                errors.put(fieldName, "Invalid value. Valid options: " + values);
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            }
        }

        errors.put("error", "JSON reading error: check field format");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
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
