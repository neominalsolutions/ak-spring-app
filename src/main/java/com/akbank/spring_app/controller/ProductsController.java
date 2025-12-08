package com.akbank.spring_app.controller;

import com.akbank.spring_app.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductsController {

    // api/v1/products
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {

     List<ProductResponse> response = Arrays.asList(new ProductResponse(UUID.randomUUID().toString(),"P-1", BigDecimal.valueOf(10.5),5), new ProductResponse(UUID.randomUUID().toString(),"P-2", BigDecimal.valueOf(15.5),5));

        return ResponseEntity.ok(response); // otomatically 200 OK -> Json format
    }
}
