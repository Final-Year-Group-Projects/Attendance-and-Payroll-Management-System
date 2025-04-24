package com.attendance;

import com.attendance.client.UserServiceClient;
import com.attendance.client.UserServiceClient.AuthRequest;
import com.attendance.client.UserServiceClient.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private UserServiceClient userServiceClient;

    @Value("${app.security.enabled:true}")
    private boolean securityEnabled;

    @SuppressWarnings("Convert2MethodRef")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("app.security.enabled value: {}", securityEnabled);
        if (securityEnabled) {
            logger.info("Security is enabled");
            http
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/attendance/**").hasRole("ADMIN")
                            .requestMatchers("/test", "/").permitAll()
                            .anyRequest().authenticated()
                    )
                    .httpBasic(Customizer.withDefaults())
                    .csrf(csrf -> csrf.disable()); // Disable CSRF
        } else {
            logger.info("Security is disabled for testing");
            http
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()
                    )
                    .httpBasic(httpBasic -> httpBasic.disable()) // Disable Basic Auth
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No sessions
                    .csrf(csrf -> csrf.disable()); // Disable CSRF
        }
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        logger.info("Initializing userDetailsService with UserServiceClient");
        return username -> {
            if (!securityEnabled) {
                logger.info("Security disabled, returning null UserDetails to bypass authentication");
                return null;
            }
            logger.info("Validating user: {}", username);
            AuthRequest authRequest = new AuthRequest();
            authRequest.setUsername(username);
            UserDTO userDTO = userServiceClient.validateUser(authRequest);
            if (userDTO == null) {
                logger.warn("User not found via user-service: {}", username);
                throw new UsernameNotFoundException("User not found via user-service: " + username);
            }
            logger.info("User validated via user-service: {} with role: {}", userDTO.getUsername(), userDTO.getRole());
            return User.withUsername(userDTO.getUsername())
                    .password("{noop}dummy")
                    .roles(userDTO.getRole())
                    .build();
        };
    }

    @Bean
    @Profile("test")
    public UserDetailsService testUserDetailsService() {
        logger.info("Initializing testUserDetailsService");
        return username -> {
            if ("admin".equals(username)) {
                logger.info("Returning mocked user: admin with role ADMIN");
                return User.withUsername("admin")
                        .password("{noop}admin123")
                        .roles("ADMIN")
                        .build();
            }
            logger.warn("User not found in test profile: {}", username);
            throw new UsernameNotFoundException("User not found: " + username);
        };
    }
}