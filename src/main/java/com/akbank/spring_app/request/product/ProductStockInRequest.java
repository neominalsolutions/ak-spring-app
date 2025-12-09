package com.akbank.spring_app.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductStockInRequest {

    @JsonProperty("product_id")
    private String id;

    @JsonProperty("stock_in_quantity") // Stoğa eklenecek olan adet
    private Integer quantity;

}
