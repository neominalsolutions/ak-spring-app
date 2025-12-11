package com.akbank.spring_app.config;

import com.akbank.spring_app.filter.AuthEntryPoint;
import com.akbank.spring_app.filter.JwtFilter;
import com.akbank.spring_app.repository.IUserRepository;
import com.akbank.spring_app.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final IUserRepository userRepository;
    private final JwtFilter jwtFilter;
    private final AuthEntryPoint authEntryPoint;

    public SecurityConfig(IUserRepository userRepository,JwtFilter jwtFilter, AuthEntryPoint authEntryPoint) {
        this.userRepository = userRepository;
        this.jwtFilter = jwtFilter;
        this.authEntryPoint = authEntryPoint;
    }

    // UserDetailsService, kullanıcı bilgilerini yüklemek için kullanılır
    // Genellikle veritabanından kullanıcı bilgilerini almak için kullanılır
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailService(userRepository); // kullanıcı yönetimini CustomUserDetailService sınıfına devrediyoruz
    }

    // AuthenticationManager, kimlik doğrulama işlemlerini yönetir, SecurityContext'e erişim sağlar. Oturum bilgilerinin SecurityContext'te saklanmasını sağlar.
    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> {
            return daoAuthenticationProvider().authenticate(authentication);
        };
    }

    // BCryptPasswordEncoder, şifreleri güvenli bir şekilde saklamak için kullanılır
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // DaoAuthenticationProvider, UserDetailsService ve PasswordEncoder'ı kullanarak kimlik doğrulama sağlar
    // Veri katmanında kullanıcı bilgilerini doğrulamak için kullanılır
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        // burada userDetailsService ve passwordEncoder ayarları yapılabilir
        return provider;
    }


    // api stateless çalıştığımız için csrf kapatıldı, normal web uygulamalarında açık kalmalı
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // production ortamında h2-console kapatılmalı çünkü güvenlik açığı oluşturur ve aynı zamanda frame options disable edilmemeli
        http.headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // h2-console için frame options disable
            .csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(a -> {
                a.requestMatchers("/api/v1/auth/**","/h2-console/**").permitAll(); // register login endpointelerine izin ver
                a.anyRequest().authenticated(); // diğer tüm endpointler için authentication gerekli
            }).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authenticationProvider(daoAuthenticationProvider()).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
        ; // stateless session yönetimi


        return http.build();
    }


    // Not: Crud işlemler yaparken eğer hata varsa 403 döndürüyor bunu düzeltmek için global exception handler eklenebilir

}
