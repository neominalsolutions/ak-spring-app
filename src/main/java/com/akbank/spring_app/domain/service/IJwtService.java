package com.akbank.spring_app.domain.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {
    String generateToken(UserDetails userDetails);
    Claims parseToken(String token);
    boolean isTokenExpired(String token);
}
