package com.Shawarma.Shawarma.controller;

import com.Shawarma.Shawarma.service.CustomUserDetailsService;
import com.Shawarma.Shawarma.util.JWTUtil;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@SecurityRequirement(name = "bearerAuth") // ✅ אומר לסוואגר שהקונטרולר הזה דורש טוקן JWT
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid AuthRequest request) {
        if (userDetailsService.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(400).body("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        userDetailsService.save(request.getUsername(), encodedPassword);
        return ResponseEntity.status(201).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            User springUser = (User) userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(springUser.getUsername());
            String username = springUser.getUsername();

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", username);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(
                    Map.of("message", "Invalid username or password")
            );
        }
    }

    // ✅ GET /auth/profile - מחזיר את שם המשתמש לפי הטוקן
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String username = authentication.getName();
        return ResponseEntity.ok(Map.of("username", username));
    }

    // DTO פנימי
    public static class AuthRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }
}
