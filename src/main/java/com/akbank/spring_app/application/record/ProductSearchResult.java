package com.akbank.spring_app.application.record;

import java.math.BigDecimal;

public record ProductSearchResult(
        String name,
        BigDecimal price,
        Integer quantity,
        String categoryname
) {}