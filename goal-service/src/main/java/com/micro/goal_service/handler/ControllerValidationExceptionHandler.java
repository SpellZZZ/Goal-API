package com.micro.goal_service.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerValidationExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleMethodArgumentException(
            MethodArgumentNotValidException exception) {
        Map<String, String> errorMap;
        errorMap = prepareMethodArgumentNotValidMessage(exception);
        return handleBadRequest(errorMap);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(
            ConstraintViolationException exception) {
        Map<String, String> errorMap;
        errorMap = prepareConstraintViolationMessage(exception);
        return handleBadRequest(errorMap);
    }

    private ResponseEntity<Map<String, String>> handleBadRequest(Map<String, String> message) {
        return ResponseEntity.badRequest().body(message);
    }

    private Map<String, String> prepareMethodArgumentNotValidMessage(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }

    private Map<String, String> prepareConstraintViolationMessage(ConstraintViolationException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getConstraintViolations().forEach(error -> {
            errorMap.put(error.getMessage(), error.getMessage());
        });
        return errorMap;
    }
}
