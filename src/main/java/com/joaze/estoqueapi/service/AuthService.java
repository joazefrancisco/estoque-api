package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.auth.LoginRequest;
import com.joaze.estoqueapi.dto.auth.LoginResponse;
import com.joaze.estoqueapi.dto.auth.RegisterRequest;
import com.joaze.estoqueapi.exception.BusinessException;
import com.joaze.estoqueapi.model.User;
import com.joaze.estoqueapi.repository.UserRepository;
import com.joaze.estoqueapi.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request){

        if (userRepository.existsByEmail(request.email())){
            throw new BusinessException("Email already registered");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .createdAd(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String token = jwtService.generateToken(request.email());

        return new LoginResponse(token);
    }
}
