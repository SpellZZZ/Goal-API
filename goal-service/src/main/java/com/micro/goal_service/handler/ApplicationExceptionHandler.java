package com.micro.goal_service.handler;

import com.micro.goal_service.exception.GoalException;
import com.micro.goal_service.exception.PaymentException;
import com.micro.goal_service.exception.PeriodException;
import com.micro.goal_service.exception.PriorityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler({GoalException.class, PaymentException.class, PriorityException.class, PeriodException.class})
    public ResponseEntity<Map<String, String>> handleNotPresentException(RuntimeException exception) {
        Map<String, String> errorMap;
        errorMap = prepareMessage(exception);

        return handleNotFound(errorMap);
    }

    private ResponseEntity<Map<String, String>> handleNotFound(Map<String, String> message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    private Map<String, String> prepareMessage(RuntimeException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(exception.getClass().getSimpleName(), exception.getMessage());
        return errorMap;
    }
}
