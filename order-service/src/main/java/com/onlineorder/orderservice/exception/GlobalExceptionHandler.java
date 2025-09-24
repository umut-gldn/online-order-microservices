package com.onlineorder.orderservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleNotFound(RuntimeException exception){
            return ResponseEntity.badRequest().body(error("BAD_REQUEST",exception.getMessage()));
    }

    private Map<String, Object> error(String code, String message){
        return Map.of("timestamp", Instant.now().toString(),
                "code", code,
                "message", message);
    }
}
