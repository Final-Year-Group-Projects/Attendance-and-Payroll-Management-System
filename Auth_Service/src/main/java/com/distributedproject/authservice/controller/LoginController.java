package com.distributedproject.authservice.controller;

import com.distributedproject.authservice.dto.LoginRequest;
import com.distributedproject.authservice.model.User;
import com.distributedproject.authservice.repository.UserRepository;
import com.distributedproject.authservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User u = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), u.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtService.generateToken(new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), List.of(new SimpleGrantedAuthority(u.getRole()))
        ));

        return ResponseEntity.ok(Map.of("accessToken", token));
    }
}
