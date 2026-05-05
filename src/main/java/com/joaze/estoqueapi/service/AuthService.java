package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.auth.LoginRequest;
import com.joaze.estoqueapi.dto.auth.LoginResponse;
import com.joaze.estoqueapi.dto.auth.RegisterRequest;
import com.joaze.estoqueapi.model.User;
import com.joaze.estoqueapi.repository.UserRepository;
import com.joaze.estoqueapi.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public void register(RegisterRequest request){

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Username  not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(token);
    }
}
