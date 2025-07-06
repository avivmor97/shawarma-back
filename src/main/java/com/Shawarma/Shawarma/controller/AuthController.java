package com.Shawarma.Shawarma.controller;

import com.Shawarma.Shawarma.model.AppUser;
import com.Shawarma.Shawarma.service.CustomUserDetailsService;
import com.Shawarma.Shawarma.service.SmsServiceClient;
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
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SmsServiceClient smsServiceClient;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid AuthRequest request) {
        if (userDetailsService.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(400).body("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        userDetailsService.save(request.getUsername(), encodedPassword, request.getPhoneNumber());

        // 📲 שליחת SMS אחרי רישום
        smsServiceClient.sendCustomSms(
                request.getPhoneNumber(),
                "🎉 Welcome " + request.getUsername() + "! You are now registered to Shawarma Land."
        );

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

            AppUser user = userDetailsService.getAppUserByUsername(username);
            String phoneNumber = user.getPhoneNumber();
            smsServiceClient.sendLoginSms(phoneNumber, username);

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

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String username = authentication.getName();
        return ResponseEntity.ok(Map.of("username", username));
    }

    public static class AuthRequest {
        private String username;
        private String password;
        private String phoneNumber;

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
        public String getPhoneNumber() {
            return phoneNumber;
        }
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
}
