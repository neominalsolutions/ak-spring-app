package com.akbank.spring_app.service;

import io.jsonwebtoken.Claims;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Primary
public class AkbankJwtService implements IJwtService{
    @Override
    public String generateToken(UserDetails userDetails) {
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzY1NTMxMDg0LCJleHAiOjE3NjU1MzI4ODR9.02fdFZOZpmQNLqz9wM45yZ-bCqDVx2sfBXG2r2wjlIKw-EEqkm05P4GCH7YBDwQH41Sm13MV_Y1zPeE0ilwlvQ";
    }

    @Override
    public Claims parseToken(String token) {
        return null;
    }

    @Override
    public boolean isTokenExpired(String token) {
        return false;
    }
}
