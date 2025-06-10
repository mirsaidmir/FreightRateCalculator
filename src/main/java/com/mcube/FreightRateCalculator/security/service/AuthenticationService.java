package com.mcube.FreightRateCalculator.security.service;

import com.mcube.FreightRateCalculator.security.dto.JwtAuthenticationResponse;
import com.mcube.FreightRateCalculator.security.dto.RegisterRequest;
import com.mcube.FreightRateCalculator.security.dto.SignInRequest;
import com.mcube.FreightRateCalculator.security.entity.User;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public JwtAuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .roles(request.getRoles())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userService.save(user);
        return buildResponse(user);
    }

    public JwtAuthenticationResponse authenticate(SignInRequest request) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        UserDetails user = userService.loadUserByUsername(request.getUsername());
        return buildResponse(user);
    }

    public JwtAuthenticationResponse refreshToken(String token) {
        if (!jwtService.isValidRefreshToken(token)) {
            throw new JwtException("Invalid or expired refresh token");
        }

        String username = jwtService.extractUsername(token);
        UserDetails user = userService.loadUserByUsername(username);

        return buildResponse(user);
    }

    private JwtAuthenticationResponse buildResponse(UserDetails user) {
        return JwtAuthenticationResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }
}
