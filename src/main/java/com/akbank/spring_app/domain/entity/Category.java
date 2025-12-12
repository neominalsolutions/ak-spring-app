package com.akbank.spring_app.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto increment integer id 1 -> 2 -> 3
    private Integer id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    // FetchType.EAGER yaparsak Category yüklendiğinde ilişkili ürünler de hemen yüklenir.
    // select c1_0.id,c1_0.name,p1_0.category_id,p1_0.id,p1_0.brand,p1_0.description,p1_0.name,p1_0.unit_price,p1_0.quantity from categories c1_0 left join products p1_0 on c1_0.id=p1_0.category_id where c1_0.id=?

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Product> products = new java.util.ArrayList<>(); // null reference hatasını önlemek için boş liste ile başlatıyoruz.

    // CascadeType.ALL -> Category silindiğinde ilişkili ürünlerde silinir.
    // FetchType.LAZY -> Category yüklendiğinde ürünler hemen yüklenmez, gerektiğinde yüklenir.
    // CascadeType.PERSIST -> Category kaydedildiğinde ilişkili ürünlerde kaydedilir.
    // CascadeType.MERGE -> Category güncellendiğinde ilişkili ürünlerde güncellenir.
    // CascadeType.REMOVE -> Category silindiğinde ilişkili ürünlerde silinir.

}
