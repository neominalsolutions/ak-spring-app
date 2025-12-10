package com.akbank.spring_app.entity;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto increment integer id 1 -> 2 -> 3
    private Integer id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;

    // CascadeType.ALL -> Category silindiğinde ilişkili ürünlerde silinir.
    // FetchType.LAZY -> Category yüklendiğinde ürünler hemen yüklenmez, gerektiğinde yüklenir.
    // CascadeType.PERSIST -> Category kaydedildiğinde ilişkili ürünlerde kaydedilir.
    // CascadeType.MERGE -> Category güncellendiğinde ilişkili ürünlerde güncellenir.
    // CascadeType.REMOVE -> Category silindiğinde ilişkili ürünlerde silinir.

}
