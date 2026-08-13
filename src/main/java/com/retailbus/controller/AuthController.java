package com.retailbus.controller;

import com.retailbus.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller.
 *
 * Provides a simple protected endpoint that the frontend calls right after
 * submitting the login form. Since HTTP Basic Auth is used (no JWT, no
 * roles), this endpoint's only job is to confirm the submitted
 * username/password pair is valid and to return the logged-in username.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Returns the currently authenticated user's details.
     * If the request reaches this method, the credentials supplied via
     * HTTP Basic Auth were valid (otherwise Spring Security would have
     * already rejected the request with a 401).
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> me(Authentication authentication) {
        Map<String, String> user = new HashMap<>();
        user.put("username", authentication.getName());

        return ResponseEntity.ok(ApiResponse.success("Authenticated", user));
    }
}

