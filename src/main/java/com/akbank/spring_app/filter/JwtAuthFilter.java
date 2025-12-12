package com.akbank.spring_app.filter;

import com.akbank.spring_app.service.CustomUserDetailService;
import com.akbank.spring_app.service.JsonWebtokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private JsonWebtokenService jwtService;
    private CustomUserDetailService customUserDetailService;
    public JwtAuthFilter(JsonWebtokenService jwtService, CustomUserDetailService customUserDetailService) {
        this.jwtService = jwtService;
        this.customUserDetailService = customUserDetailService;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JwtFilter");

        // gelen token üzerinden oturum doğrulama işlemleri yapılmalıdır.

        String header = request.getHeader("Authorization");

        if(header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("Token: " + token);
            // Token doğrulama ve kullanıcı bilgilerini alma işlemleri burada yapılabilir
            try{
               Claims claims =  jwtService.parseToken(token);
               System.out.println("Token doğrulandı. Claims: " + claims);

               boolean tokenExpired =  jwtService.isTokenExpired(token);

               if(!tokenExpired && claims != null){
                   // Token geçerli, kullanıcı bilgilerini al
                   String username = claims.getSubject();
                   System.out.println("Kullanıcı adı: " + username);
                   UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
                   // UserDetails bilgilerini kullanarak anlık oturum açma işlemleri yapılabilir

                   UsernamePasswordAuthenticationToken authToken =
                           new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                   SecurityContextHolder.getContext().setAuthentication(authToken); // sistemde authenticated olarak kendimiz ayarlıyoruz.
                   // Burada kullanıcı bilgilerini güvenlik bağlamına ekleyebilirsiniz
                   filterChain.doFilter(request, response);
               } else {
                   System.out.println("Token süresi dolmuş veya geçersiz.");
                   filterChain.doFilter(request, response);
               }

            } catch (RuntimeException e){
                // Eğer JWT filtresi içinde veya sonrasında bir RuntimeException oluşursa,
                // (500'e neden olacak hatalar dahil) burada yakalanır.

                // Hata detaylarını JSON olarak ayarla
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                final Map<String, Object> errorDetails = new HashMap<>();
                errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                errorDetails.put("error", "Internal Server Error");
                // Güvenlik nedeniyle e.getMessage()'ı doğrudan döndürmemeyi düşünebilirsiniz.
                errorDetails.put("message", "Uygulama içinde beklenmedik bir hata oluştu: " + e.getMessage());
                errorDetails.put("path", request.getRequestURI());

                // Yanıtı yaz
                new ObjectMapper().writeValue(response.getWriter(), errorDetails);
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }
}
