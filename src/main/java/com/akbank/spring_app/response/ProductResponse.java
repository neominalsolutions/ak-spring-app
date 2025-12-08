package com.akbank.spring_app.response;

import lombok.*;
import java.math.BigDecimal;


@NoArgsConstructor // default constructor
@AllArgsConstructor // contructor with all parameters
@Data // Auto Getter Setter
public class ProductResponse {
    private String id;
    private  String name;
    private BigDecimal price;
    private Integer quantity;
}
