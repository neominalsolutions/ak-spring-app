package com.akbank.spring_app.entity;


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
}
