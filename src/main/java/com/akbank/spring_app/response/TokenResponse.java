package com.akbank.spring_app.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String bearerToken;
}

// -H Authorization: Bearer Token