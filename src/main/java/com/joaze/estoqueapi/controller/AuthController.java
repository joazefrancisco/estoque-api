package com.joaze.estoqueapi.controller;

import com.joaze.estoqueapi.dto.auth.LoginRequest;
import com.joaze.estoqueapi.dto.auth.LoginResponse;
import com.joaze.estoqueapi.dto.auth.RegisterRequest;
import com.joaze.estoqueapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody  @Valid RegisterRequest request){
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody  @Valid LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }
}