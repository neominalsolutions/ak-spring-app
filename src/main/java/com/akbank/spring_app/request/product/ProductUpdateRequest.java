package com.akbank.spring_app.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductUpdateRequest {

    @JsonProperty("product_id")
    private String id;

    @JsonProperty("product_name")
    private String name;


    @JsonProperty("product_description")
    private String description;


}


// Stocktan düşme işlemi için ayrı bir istek sınıfı oluşturulabilir.
// Örneğin: ProductStockOutRequest
// package com.akbank.spring_app.request.product;
// Fiyat artışı için ayrı bir istek sınıfı oluşturulabilir.
// Örneğin: ProductPriceUpdateRequest
// package com.akbank.spring_app.request.product;