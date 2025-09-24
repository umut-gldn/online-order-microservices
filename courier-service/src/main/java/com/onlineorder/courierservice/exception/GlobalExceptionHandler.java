package com.onlineorder.courierservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error("NOT_FOUND",exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException exception){
        String msg=exception.getBindingResult().getFieldErrors().stream()
                .map(f->f.getField()+" "+f.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error("VALIDATION_ERROR",msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(error("INTERNAL_SERVER_ERROR",exception.getMessage()));
    }


    private Map<String, Object> error(String code, String message){
        return Map.of("timestamp", Instant.now().toString(),
                "code", code,
                "message", message);
    }
}
