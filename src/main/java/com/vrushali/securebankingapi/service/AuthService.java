package com.vrushali.securebankingapi.service;

import com.vrushali.securebankingapi.dto.AuthResponse;
import com.vrushali.securebankingapi.dto.JwtAuthResponse;
import com.vrushali.securebankingapi.dto.LoginRequest;
import com.vrushali.securebankingapi.dto.RegisterRequest;
import com.vrushali.securebankingapi.entity.Role;
import com.vrushali.securebankingapi.entity.User;
import com.vrushali.securebankingapi.exception.EmailAlreadyExistsException;
import com.vrushali.securebankingapi.exception.InvalidCredentialsException;
import com.vrushali.securebankingapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException(
                    "An account already exists with this email"
            );
        }

        User user = User.builder()
                .fullName(request.getFullName().trim())
                .email(normalizedEmail)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                "Customer registered successfully"
        );
    }

    public JwtAuthResponse login(LoginRequest request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            normalizedEmail,
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException exception) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        String token = jwtService.generateToken(user);

        return new JwtAuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getEmail(),
                user.getRole(),
                "Login successful"
        );
    }
}