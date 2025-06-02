package com.distributedproject.authservice.service;

import com.distributedproject.authservice.dto.LoginRequest;
import com.distributedproject.authservice.exception.InvalidCredentialsException;
import com.distributedproject.authservice.model.User;
import com.distributedproject.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public Map<String, String> login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUserId())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid userid"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUserId(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );

        String token = jwtService.generateToken(userDetails);

        return Map.of("accessToken", token);
    }
}
