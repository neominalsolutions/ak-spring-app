package com.akbank.spring_app.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandling {

    // MethodArgumentNotValidException böyle bir durum ile karşılaşıldığında fırlatılır.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,List<Object>>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, List<Object>> errors = new HashMap<>();

        // ["name": ["error1", "error2"], "price": ["error1"]]

        // field name yerine json property name reflect kullanılarak alınabilir miyiz ? 

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.putIfAbsent(error.getField(), new java.util.ArrayList<>());
            errors.get(error.getField()).add(error.getDefaultMessage());
        });

        return ResponseEntity.status(400).body(errors);
    }


}
