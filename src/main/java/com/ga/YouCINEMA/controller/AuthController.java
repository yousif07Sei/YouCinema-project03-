package com.ga.YouCINEMA.controller;

import com.ga.YouCINEMA.dto.request.LoginRequest;
import com.ga.YouCINEMA.dto.request.RegisterRequest;

import com.ga.YouCINEMA.dto.response.AuthenticatedUserResponse;
import com.ga.YouCINEMA.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<AuthenticatedUserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticatedUserResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }


    @GetMapping("/verify-email")
    public ResponseEntity<AuthenticatedUserResponse> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }
}
