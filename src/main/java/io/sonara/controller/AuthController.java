package io.sonara.controller;

import io.sonara.dto.LoginRequestDTO;
import io.sonara.dto.LoginResponseDTO;
import io.sonara.dto.UserRegisterRequestDTO;
import io.sonara.dto.UserResponseDTO;
import io.sonara.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@RequestBody @Valid UserRegisterRequestDTO request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO request) {
        return authService.login(request);
    }
}