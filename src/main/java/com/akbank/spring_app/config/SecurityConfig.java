package com.akbank.spring_app.config;

import com.akbank.spring_app.repository.IUserRepository;
import com.akbank.spring_app.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final IUserRepository userRepository;

    public SecurityConfig(IUserRepository userRepository) {
        this.userRepository = userRepository;
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
    public BCryptPasswordEncoder passwordEncoder() {
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
        http
            .csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(a -> {
                a.requestMatchers("/api/v1/auth/**").permitAll(); // register login endpointelerine izin ver
                a.anyRequest().authenticated(); // diğer tüm endpointler için authentication gerekli
            }).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authenticationProvider(daoAuthenticationProvider()); // stateless session yönetimi

        return http.build();
    }


}
