package com.akbank.spring_app.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;


// JWT Token oluşturma ve doğrulama işlemlerini gerçekleştirecek servis sınıfı

@Service
public class JsonWebtokenService implements IJwtService {

    private String key = "b41d5cf12df3c302fee644c0e06fd62a9b6a9f496773806d92170ad274b164ab5e27686377545ae1e7281592758f14dbccc6eb67c067f2971999402a00f5ff2t";

    public String generateToken(UserDetails userDetails) {
       return io.jsonwebtoken.Jwts.builder()
               .setSubject(userDetails.getUsername()) // token'ın konusu (kullanıcı adı)
               .setIssuedAt(new Date(System.currentTimeMillis())) // token oluşturulma zamanı
              .setExpiration(new Date(System .currentTimeMillis() +  1000 * 60 * 30)) // 30 dakika geçerli
               .signWith(SignatureAlgorithm.HS512, Keys.hmacShaKeyFor(key.getBytes())).compact(); // token imzalama
    }

    public Claims parseToken(String token) {
        return io.jsonwebtoken.Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key.getBytes())) // imzalama anahtarı
                .build()
                .parseClaimsJws(token)
                .getBody(); // token içindeki iddialar (claims)
    }

    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(new Date());
    }

}
