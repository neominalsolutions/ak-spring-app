package com.akbank.spring_app.domain.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;


// Şema oluşturulurken birden fazla alan için tek bir unique constraint tanımlanabilir.
// @UniqueConstraint(name = "UniqueBrandAndDescription", columnNames = { "brand", "description" })} bunu kullanabiliriz fakat eğer tablo generated ise @IdClass anatation kullanmak daha mantıklıdır.
// https://medium.com/@danaprata/spring-data-jpa-composite-primary-key-75aa67098ee6

@Entity
@Data
@Table(name = "products", uniqueConstraints =
        { //other constraints
                @UniqueConstraint(name = "UniqueBrandAndDescription", columnNames = { "brand", "description" })})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @Column(length = 50)
    private String brand;

    @Column(length = 50)
    private String description;

    @Column(name = "unitPrice",nullable = false, precision = 16, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "category_id")
    private Integer categoryId;

    // ürünün atanımış olduğu kategori
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;
}

// Entity ilişkileri çift taraflı bidirectional olabilir isteğe bağlı tek taraflı unidirectional olabilir.
// çift taraflı ilişkilerin kullanılmasının sebebi ekranlarda nasıl bir soru talebinde bulunacağını geliştirme yaparken kestiremeyiz . -> Category Select yapılırken category ile birlikte ilişki ürünlerin ekrana getirilmesi istenebilir. veya ürün select yapılırken ürün ile birlikte kategori bilgilerinin getirilmesi istenebilir.
