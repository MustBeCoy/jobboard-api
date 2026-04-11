package com.jobboardapi.service;

import com.jobboardapi.dto.AuthResponse;
import com.jobboardapi.dto.LoginRequest;
import com.jobboardapi.dto.RegisterRequest;
import com.jobboardapi.entity.Role;
import com.jobboardapi.entity.User;
import com.jobboardapi.repository.UserRepository;
import com.jobboardapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getRole().name(), "Registered successfully!");
    }

    public AuthResponse login(LoginRequest request) {
        // This validates email + password automatically
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getRole().name(), "Login successful!");
    }
}