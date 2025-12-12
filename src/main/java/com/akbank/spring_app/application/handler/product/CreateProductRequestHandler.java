package com.akbank.spring_app.application.handler.product;

import com.akbank.spring_app.domain.entity.Product;
import com.akbank.spring_app.application.request.product.ProductCreateRequest;
import com.akbank.spring_app.application.response.ProductDetailResponse;
import com.akbank.spring_app.domain.service.IProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CreateProductRequestHandler {

    private final IProductService productService;

    public CreateProductRequestHandler(IProductService productService) {
        this.productService = productService;
    }

    public ProductDetailResponse handle(ProductCreateRequest request) {

        Product entity = new Product();
        BeanUtils.copyProperties(request, entity);
        this.productService.Create(entity);
        ProductDetailResponse response = new ProductDetailResponse();
        BeanUtils.copyProperties(entity, response);


        return response;
    }

}
