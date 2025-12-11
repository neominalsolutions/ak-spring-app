package com.akbank.spring_app.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


// Uygulamada authentication hataları için kullanılacak sınıf -> Tüm authentication hatalarını merkezi yönettiğimiz bir sınıf.
// Hangi durumda 403 hangi durumda 401 döndüreceğimizi burada yazmalıyız.
@Component
@Slf4j // Logları yönetmek için controller seviyesinde bu anatasyondan yararlandık.
public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("authEntryPoint" + authException.getMessage(), authException);

        // Not: Uygulamada herhangi exception oluşturğunda Spring Security hataları 403 veya 401 formatında gönderiyor. TODO: Bunu global exception handler ile JSON formatında dönebiliriz.

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

    }
}