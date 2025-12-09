package com.akbank.spring_app.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.math.BigDecimal;

@Data
public class ProductCreateRequest {

    @JsonProperty("product_name")
    @NotNull(message = "Product name cannot be null") // null olamaz
    @NotEmpty(message = "Product name cannot be empty") // "" boş olmaz
    @NotBlank(message = "Product name cannot be blank") // "   " boşluklardan oluşamaz)
    @Size(min = 10, max = 50, message = "Product name must be between 10 and 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Product name can only contain alphanumeric characters and spaces")
    private String name;

    // Optional field validasyon uygulamayız
    @JsonProperty("product_description")
    private String description;


    @JsonProperty("unit_price")
    @Min(value = 1, message = "Price must be at least 1")
    private BigDecimal price;

    @JsonProperty("available_quantity")
    @Min(value = 10, message = "Quantity must be at least 10")
    @Max(value = 100, message = "Quantity must be at most 100")
    private Integer quantity;
}
