package com.akbank.spring_app.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


// Uygulamada authentication hataları için kullanılacak sınıf -> Tüm authentication hatalarını merkezi yönettiğimiz bir sınıf.
// Hangi durumda 403 hangi durumda 401 döndüreceğimizi burada yazmalıyız.
@Component
@Slf4j // Logları yönetmek için controller seviyesinde bu anatasyondan yararlandık.
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("authEntryPoint" + authException.getMessage(), authException);


        // Hata yanıtını JSON olarak döndürmek için
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);

        // Bu kısımda hangi JWT hatası olduğunu belirlemek daha zordur.
        // Genellikle JWT filtresi, AuthenticationException'ı bu noktaya taşımadan önce
        // hatayı loglar. Ancak genellikle "Invalid Token" veya "Missing Token"
        // şeklinde genel bir mesajla yanıt verilir.
        body.put("error", "Unauthorized");
        body.put("message", "JWT Token yok veya geçersiz: " + authException.getMessage());
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);

    }
}