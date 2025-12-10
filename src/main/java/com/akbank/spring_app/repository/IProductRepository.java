package com.akbank.spring_app.repository;

import com.akbank.spring_app.entity.Product;
import com.akbank.spring_app.record.ProductSearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, String> {

    @Query(value = "select * from search_products_fn(:productName, :categoryName)", nativeQuery = true)
    List<ProductSearchResult> searchProducts(@Param("productName") String productName,
                                               @Param("categoryName") String categoryName);

}
