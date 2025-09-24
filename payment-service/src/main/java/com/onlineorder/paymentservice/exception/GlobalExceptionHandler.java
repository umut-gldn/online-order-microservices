package com.onlineorder.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleNotFound(RuntimeException exception){
        return ResponseEntity.badRequest().body(error("BAD_REQUEST",exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException exception){
        String msg=exception.getBindingResult().getFieldErrors().stream()
                .map(f->f.getField()+" "+f.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error("VALIDATION_ERROR",msg));
    }

    private Map<String, Object> error(String code, String message){
        return Map.of("timestamp", Instant.now().toString(),
                "code", code,
                "message", message);
    }

}
