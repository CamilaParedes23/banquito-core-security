package com.banquito.platform.identity.api.controller;

import com.banquito.platform.identity.api.dto.api.*;
import com.banquito.platform.identity.application.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        return authenticationService.login(request, ip(httpRequest), httpRequest.getHeader("User-Agent"));
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@Valid @RequestBody RefreshTokenRequest request, HttpServletRequest httpRequest) {
        return authenticationService.refresh(request, ip(httpRequest), httpRequest.getHeader("User-Agent"));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/logout")
    public void logout(@RequestBody(required = false) LogoutRequest request, HttpServletRequest httpRequest) {
        authenticationService.logout(request, ip(httpRequest), httpRequest.getHeader("User-Agent"));
    }

    @PostMapping("/introspect")
    public TokenIntrospectionResponse introspect(@Valid @RequestBody TokenIntrospectionRequest request) {
        return authenticationService.introspect(request);
    }

    @PostMapping("/client-token")
    public TokenResponse clientToken(@Valid @RequestBody ClientTokenRequest request, HttpServletRequest httpRequest) {
        return authenticationService.clientToken(request, ip(httpRequest), httpRequest.getHeader("User-Agent"));
    }

    private String ip(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
