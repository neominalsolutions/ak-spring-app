package com.akbank.spring_app.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {

    @JsonProperty("product_id")
    private String id;

    @JsonProperty("product_name")
    private String name;

    @JsonProperty("product_description")
    private String description;

    @JsonProperty("unit_price")
    private BigDecimal price;

    @JsonProperty("available_quantity")
    private Integer quantity;

}
