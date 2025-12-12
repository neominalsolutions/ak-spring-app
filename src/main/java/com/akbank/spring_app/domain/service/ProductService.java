package com.akbank.spring_app.domain.service;

import com.akbank.spring_app.domain.entity.Product;
import com.akbank.spring_app.infra.repository.IProductRepository;
import org.springframework.stereotype.Service;


@Service
public class ProductService implements IProductService {

    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void Create(Product product) {

        // aynı gün 3den fazla para ödeme işlemi yapıldımı limit aşımı var mı ?
        // aynı isimde ürün var mı kontrol et
        productRepository.findByName(product.getName()).ifPresent(existingProduct -> {
            throw new IllegalArgumentException("Product with name " + product.getName() + " already exists.");
        });
        productRepository.save(product);
    }


}
