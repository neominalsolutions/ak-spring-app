package com.akbank.spring_app.infra.repository;

import com.akbank.spring_app.domain.entity.Product;
import com.akbank.spring_app.application.record.ProductSearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<Product, String> {

    @Query(value = "select * from search_products_fn(:productName, :categoryName)", nativeQuery = true)
    List<ProductSearchResult> searchProducts(@Param("productName") String productName,
                                               @Param("categoryName") String categoryName);


    Optional<Product> findByName(String name);
}
