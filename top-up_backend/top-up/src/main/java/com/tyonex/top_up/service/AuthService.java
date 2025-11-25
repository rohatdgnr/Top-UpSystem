package com.tyonex.top_up.service;

import com.tyonex.top_up.dto.request.CompanyRequest;
import com.tyonex.top_up.dto.request.LoginRequest;
import com.tyonex.top_up.dto.request.RegisterRequest;
import com.tyonex.top_up.dto.response.AuthResponse;
import com.tyonex.top_up.entity.User;
import com.tyonex.top_up.exception.ResourceNotFoundException;
import com.tyonex.top_up.repository.UserRepository;
import com.tyonex.top_up.security.CustomUserDetailsService;
import com.tyonex.top_up.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final CompanyService companyService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .status(User.UserStatus.ACTIVE)
                .build();

        user = userRepository.save(user);

        // Create company for the user
        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setTaxNo(request.getTaxNo());
        companyRequest.setTitle(request.getCompanyTitle());
        companyService.createCompany(user.getId(), companyRequest);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtTokenProvider.generateToken(userDetails, user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtTokenProvider.generateToken(userDetails, user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}

