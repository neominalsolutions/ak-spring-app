package com.akbank.spring_app.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "unitPrice",nullable = false, precision = 16, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;
}
