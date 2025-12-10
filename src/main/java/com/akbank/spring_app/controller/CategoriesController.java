package com.akbank.spring_app.controller;


import com.akbank.spring_app.entity.Category;
import com.akbank.spring_app.entity.Product;
import com.akbank.spring_app.record.ProductSearchResult;
import com.akbank.spring_app.repository.ICategoryRepository;
import com.akbank.spring_app.repository.IProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Full Entity ile çalıştık doğru bir geliştirme değil. Dto tercih edilmelidir.

@RestController
@RequestMapping("api/v1/categories")
public class CategoriesController {

    // Senaryo DTO kullanımı yapmammanın sakıncaları nelerdir?
    // ORM toollarının Persist işlemlerdeki gücü nedir?

    private final ICategoryRepository categoryRepository;
    private  final IProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public CategoriesController(ICategoryRepository categoryRepository, IProductRepository productRepository, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    // Get Category by Id including Products /api/v1/categories?id=1
    // QueryString kullandığımızda parametre isimleri opsiyonel olur route /api/v1/categories veya /api/v1/categories?id=1
    // Eğer parametre isimleri önemli ise @PathVariable kullanılır /api/v1/categories/1
    @GetMapping
    public Category getCategoryById(@RequestParam Integer id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.get();
    }

    // api/v1/categories/byname?name=.Dr
    @GetMapping("byname")
    public Category getCategoryByName(@RequestParam String name) {
        Optional<Category> category = categoryRepository.findByNameIgnoreCaseOrderByNameDesc(name);

        // @Query kullanımı ile ilgili örnek
        Optional<Category> categoryCustom = categoryRepository.findByNameCustomQuery(name);


        return categoryCustom.get();
    }


    @GetMapping("q")
    public ResponseEntity<List<Category>> getCategoryByNamePagination(@RequestParam String name,
                                                @RequestParam int page,
                                                @RequestParam int size)  {

        page = page -1; // sayfa numarası 0'dan başlar.
        // size kaç adet veri çekileceği ile ilgiliner. withPage ise hangi sayfanın çekileceği ile ilgiliner.
        Pageable pageable=  PageRequest.of(page, size, Sort.by("name").descending());

        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(name,pageable);

        return ResponseEntity.ok(categories);
    }

    // select c1_0.id,c1_0.name from categories c1_0 where upper(c1_0.name) like upper(?) escape '\' order by c1_0.name desc fetch first ? rows only

    @PostMapping
    @Transactional
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {

        // Not: CategoryId Identity yaptığımız senaryolarda, önce Category kaydedilmeli ki ID değeri oluşsun.
        // Sonrasında 2. bir sorgu ile Category'e Product atama işlemi yapılıp update sorgusu çalışır.
        Category savedCategory = categoryRepository.save(category); // Create


        Product p1 = new Product();
        p1.setName("Test Product " + UUID.randomUUID().toString());
        p1.setDescription("This is a test product");
        p1.setPrice(BigDecimal.valueOf(100.0));
        p1.setQuantity(20);
        p1.setCategoryId(savedCategory.getId());


        Product p2 = new Product();
        p2.setName("Test Product " + UUID.randomUUID().toString());
        p2.setDescription("This is a test product");
        p2.setPrice(BigDecimal.valueOf(120.0));
        p2.setQuantity(24);
        p2.setCategoryId(savedCategory.getId());

        category.getProducts().add(p1);
        category.getProducts().add(p2);

        categoryRepository.save(category); // Update


        Optional<Category> response =  categoryRepository.findById(category.getId());

        return ResponseEntity.ok(response.get());

    }

    // Eğer üst tablo silersek alt tablodaki ilişkili kayıtları da silmek istersek ne yapmalıyız?
    // Burada CascadeType kullanımı nasıl bir rol oynar?
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Integer id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    // Not: Postgres Bağlantısı gerektirir
    @GetMapping("/searchProducts")
    public ResponseEntity<List<ProductSearchResult>> searchProductsByName(@RequestParam String productName,@RequestParam String categoryName) {

      List<ProductSearchResult> response =  this.productRepository.searchProducts(productName,categoryName);

        return ResponseEntity.ok(response);

    }


}
