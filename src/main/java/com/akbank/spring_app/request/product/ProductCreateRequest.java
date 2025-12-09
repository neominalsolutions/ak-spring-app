package com.akbank.spring_app.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCreateRequest {

    @JsonProperty("product_name")
    private String name;

    @JsonProperty("product_description")
    private String description;

    @JsonProperty("unit_price")
    private BigDecimal price;

    @JsonProperty("available_quantity")
    private Integer quantity;
}
