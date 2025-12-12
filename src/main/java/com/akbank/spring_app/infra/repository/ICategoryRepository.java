package com.akbank.spring_app.infra.repository;

import com.akbank.spring_app.domain.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Integer> {

    // Find Category by name ignoring case and order by price, küçük büyük harf duyarsız
    // parametre değerlerini field isimleri üzerinden almaız gerekir name fieldname
    Optional<Category> findByNameIgnoreCaseOrderByNameDesc(String name);

    // Not: Tek kayıt veri çekme işlemlerinde Optional<T> kullanımı tavsiye edilir.
    // test Test TesT -> Categoryname

    @Query("SELECT c FROM Category c WHERE LOWER(c.name) = LOWER(?1)")
    Optional<Category> findByNameCustomQuery(String name);

    // Pagination ve Sorting için JpaRepository'den gelen metodlar kullanılabilir.
    List<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);


}

