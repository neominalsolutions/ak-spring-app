package com.akbank.spring_app.filter;

import com.akbank.spring_app.service.CustomUserDetailService;
import com.akbank.spring_app.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private CustomUserDetailService customUserDetailService;
    public JwtFilter(JwtService jwtService, CustomUserDetailService customUserDetailService) {
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
                   filterChain.doFilter(request, response);

                   // Burada kullanıcı bilgilerini güvenlik bağlamına ekleyebilirsiniz
               } else {
                   System.out.println("Token süresi dolmuş veya geçersiz.");
                   filterChain.doFilter(request, response);
               }

            } catch (Exception e){
                System.out.println("Token doğrulama hatası: " + e.getMessage());
            }

        }

        filterChain.doFilter(request, response);
    }
}
