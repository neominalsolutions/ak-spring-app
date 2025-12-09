package com.akbank.spring_app.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductDeleteByNameRequest {
    @JsonProperty("product_id")
    private String id;

    @JsonProperty("product_name")
    private String name;
}
