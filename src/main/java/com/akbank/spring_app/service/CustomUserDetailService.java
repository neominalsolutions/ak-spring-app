package com.akbank.spring_app.service;

import com.akbank.spring_app.repository.IUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final IUserRepository userRepository;


    public CustomUserDetailService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Repositoryden ilgili user UserName göre bulmamız lazım.

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
