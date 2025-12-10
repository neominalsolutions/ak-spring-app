package com.akbank.spring_app.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandling {

    // MethodArgumentNotValidException böyle bir durum ile karşılaşıldığında fırlatılır.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,List<Object>>> handleValidationExceptions(MethodArgumentNotValidException ex)  {

        Map<String, List<Object>> errors = new HashMap<>();

        // ["name": ["error1", "error2"], "price": ["error1"]]
        // field name yerine json property name reflect kullanılarak alınabilir miyiz ?

        ex.getBindingResult().getFieldErrors().forEach(error -> {

            String jsonFieldName = resolveJsonPropertyName(error, ex.getBindingResult().getTarget().getClass());
            errors.putIfAbsent(jsonFieldName, new java.util.ArrayList<>());
            errors.get(jsonFieldName).add(error.getDefaultMessage());

        });

        return ResponseEntity.status(400).body(errors);
    }


    private static String resolveJsonPropertyName(FieldError error, Class<?> targetClass) {
        try {
            Field field = targetClass.getDeclaredField(error.getField());
            JsonProperty annotation = field.getAnnotation(JsonProperty.class);
            if (annotation != null && !annotation.value().isBlank()) {
                return annotation.value();  // JSON’daki isim
            }
        } catch (NoSuchFieldException ignored) {}
        return error.getField(); // annotation yoksa field name
    }


}
