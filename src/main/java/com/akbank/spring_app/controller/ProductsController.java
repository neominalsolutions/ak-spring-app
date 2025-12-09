package com.akbank.spring_app.controller;

import com.akbank.spring_app.entity.Product;
import com.akbank.spring_app.repository.IProductRepository;
import com.akbank.spring_app.request.product.ProductCreateRequest;
import com.akbank.spring_app.request.product.ProductDeleteByNameRequest;
import com.akbank.spring_app.request.product.ProductStockInRequest;
import com.akbank.spring_app.request.product.ProductUpdateRequest;
import com.akbank.spring_app.response.ProductDetailResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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


    private final IProductRepository productRepository;

    // Dependency Injection (DI) -> Inversion of Control (IoC)
    public ProductsController(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // api/v1/products
    @GetMapping
    public ResponseEntity<List<ProductDetailResponse>> getProducts() {

//     List<ProductResponse> response = Arrays.asList(new ProductResponse(UUID.randomUUID().toString(),"P-1", BigDecimal.valueOf(10.5),5), new ProductResponse(UUID.randomUUID().toString(),"P-2", BigDecimal.valueOf(15.5),5));


        List<ProductDetailResponse> response = productRepository.findAll().stream()
                .map(product -> new ProductDetailResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getQuantity()
                ))
                .toList();


        return ResponseEntity.ok(response); // otomatically 200 OK -> Json format
    }

    // @PathVariable -> Path üzerinden dinamik veri almayı sağlar. GET isteklerinde urlden hassas olmayan veriler taşınabilir.
    // api/v1/products/{id} // api/v1/products/1
    @GetMapping("{id}")
    public ResponseEntity<ProductDetailResponse> getProductById(@PathVariable String id) {
//        ProductResponse response = new ProductResponse(id,"P-1", BigDecimal.valueOf(10.5),5);

//        Product p =  productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
//
//        ProductResponse response = new ProductResponse(
//                p.getId(),
//                p.getName(),
//                p.getPrice(),
//                p.getQuantity()
//        );

        // 2. yöntem

        Optional<Product> p2 = productRepository.findById(id);

        if(p2.isEmpty()){
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        ProductDetailResponse response = new ProductDetailResponse(
                p2.get().getId(),
                p2.get().getName(),
                p2.get().getDescription(),
                p2.get().getPrice(),
                p2.get().getQuantity()
        );

        return ResponseEntity.ok(response); // otomatically 200 OK -> Json format
    }

    // @RequestBody -> İstek gövdesinden veri almayı sağlar. POST, PUT, DELETE isteklerinde kullanılır.
    // api/v1/products -> Güvenli Hassas veriler @RequestBody ile taşınır.
    @PostMapping
    public ResponseEntity<ProductDetailResponse> createProduct(@RequestBody ProductCreateRequest request) {
        // DB kaydetme işlemleri yapılır.

        Product entity = new Product();
        BeanUtils.copyProperties(request, entity);
        productRepository.save(entity);
        // Kayıt edilen entityden id yok id ve diğer bilgileri aldık dto attık. Dto olarak return ettik.
        ProductDetailResponse response = new ProductDetailResponse();
        BeanUtils.copyProperties(entity, response);

        return ResponseEntity.status(201).body(response); // 201 Created
    }

    // Yanış endoint tanımları -> api/v1/products/update/{id}, api/v1/createProduct // api/v1/product-delete/{id}
    @PutMapping("{id}") // Resource Update
    public ResponseEntity<Void> updateProduct(@PathVariable String id, @RequestBody ProductUpdateRequest request) {

        // parametre olarak gelen id ile request içindeki id aynı mı kontrolü yapılır.
        if(!id.equals(request.getId())){
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        Optional<Product> product= productRepository.findById(id);

        if(product.isEmpty()){
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        Product entity = product.get();
        BeanUtils.copyProperties(request, entity);
        productRepository.save(entity);

        // DB güncelleme işlemleri yapılır.

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}") // Resource Delete
    public ResponseEntity<Void> deleteProduct(@PathVariable String id, @RequestBody ProductDeleteByNameRequest request) {
        // DB silme işlemleri yapılır.

        // Burada ekstra bir kontrol yapılmalalıdır.

        Optional<Product> product= productRepository.findById(id);
        if(product.isEmpty()){
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        productRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{productId}/stockIn") // Resource Partial Update
    public ResponseEntity<Void> stockInProduct(@PathVariable String productId, @RequestBody ProductStockInRequest request) {
        Optional<Product> product= productRepository.findById(productId);

        if(product.isEmpty()){
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        Product entity = product.get();
        entity.setQuantity(entity.getQuantity() + request.getQuantity());
        productRepository.save(entity);

        return ResponseEntity.noContent().build();
    }

}
