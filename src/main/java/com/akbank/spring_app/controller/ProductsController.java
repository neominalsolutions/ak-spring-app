package com.akbank.spring_app.controller;

import com.akbank.spring_app.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

// Client isteği yanlış gönderim yaptı
// 405 Method Not Allowed -> Yanlış HTTP metodu kullanımı
// 415 Unsupported Media Type -> Yanlış içerik tipi kullanımı hatalarını JSON formatında dönelinm.
// 404 Not Found
// {"name12":"{{$randomProduct}}","price":{{$randomPrice}},"quantity":"sadsad"} 400 Bad Request
//
// @ControllerAdvice ile global exception handling yapılabilir.

// 401 ile 403 Spring Security ile ilgilidir. -> Authentication ve Authorization hatalarıdır.

@RestController
@RequestMapping("/api/v1/products")
public class ProductsController {

    // api/v1/products
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {

     List<ProductResponse> response = Arrays.asList(new ProductResponse(UUID.randomUUID().toString(),"P-1", BigDecimal.valueOf(10.5),5), new ProductResponse(UUID.randomUUID().toString(),"P-2", BigDecimal.valueOf(15.5),5));

        return ResponseEntity.ok(response); // otomatically 200 OK -> Json format
    }

    // @PathVariable -> Path üzerinden dinamik veri almayı sağlar. GET isteklerinde urlden hassas olmayan veriler taşınabilir.
    // api/v1/products/{id} // api/v1/products/1
    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
        ProductResponse response = new ProductResponse(id,"P-1", BigDecimal.valueOf(10.5),5);

        return ResponseEntity.ok(response); // otomatically 200 OK -> Json format
    }

    // @RequestBody -> İstek gövdesinden veri almayı sağlar. POST, PUT, DELETE isteklerinde kullanılır.
    // api/v1/products -> Güvenli Hassas veriler @RequestBody ile taşınır.
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductResponse request) {
        // DB kaydetme işlemleri yapılır.
        request.setId(UUID.randomUUID().toString());

        return ResponseEntity.status(201).body(request); // 201 Created
    }

    // Yanış endoint tanımları -> api/v1/products/update/{id}, api/v1/createProduct // api/v1/product-delete/{id}
    @PutMapping("{id}") // Resource Update
    public ResponseEntity<Void> updateProduct(@PathVariable String id, @RequestBody ProductResponse request) {
        // DB güncelleme işlemleri yapılır.

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}") // Resource Delete
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        // DB silme işlemleri yapılır.

        return ResponseEntity.noContent().build();
    }

}
