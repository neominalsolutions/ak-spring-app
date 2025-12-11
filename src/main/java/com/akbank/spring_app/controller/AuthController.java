package com.akbank.spring_app.controller;

import com.akbank.spring_app.entity.User;
import com.akbank.spring_app.repository.IUserRepository;
import com.akbank.spring_app.request.auth.LoginRequest;
import com.akbank.spring_app.request.auth.RegisterRequest;
import com.akbank.spring_app.response.TokenResponse;
import com.akbank.spring_app.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(IUserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest   request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        // AbstractUserDetailsAuthenticationProvider -> authenticate metodu çağrılır
        Authentication auth = authenticationManager.authenticate(authenticationToken);

        if(!auth.isAuthenticated()){
            return ResponseEntity.status(401).body("Authentication failed");
        }

        String token = this.jwtService.generateToken((UserDetails)auth.getPrincipal());


        // JWT Generate  -> Return JWT Token
        return ResponseEntity.ok(new TokenResponse(token));
    }

}
