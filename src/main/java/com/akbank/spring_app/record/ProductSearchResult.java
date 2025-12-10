package com.akbank.spring_app.record;

import java.math.BigDecimal;

public record ProductSearchResult(
        String name,
        BigDecimal price,
        Integer quantity,
        String categoryname
) {}