package com.Shawarma.Shawarma.config;


import org.springframework.http.HttpMethod;

import com.Shawarma.Shawarma.service.CustomUserDetailsService;
import com.Shawarma.Shawarma.util.JWTFilter;
import com.Shawarma.Shawarma.error.JWTAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JWTFilter jwtFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // הוספת CORS - הגדרת אפשרות CORS
        http.cors(cors -> cors.configurationSource(request -> {
            var source = new org.springframework.web.cors.CorsConfiguration();
            source.addAllowedOrigin("http://localhost:5173");  // כתובת ה-Frontend שלך
            source.addAllowedMethod("GET");
            source.addAllowedMethod("POST");
            source.addAllowedMethod("PUT");
            source.addAllowedMethod("DELETE");
            source.addAllowedHeader("*");
            source.setAllowCredentials(true);  // אם אתה שולח cookies (כמו JWT)
            return source;
        }));

        // מכבה את CSRF בגרסה החדשה
        http.csrf(csrf -> csrf.disable());  // הגדרה נכונה לכיבוי CSRF

        // הגדרת session management (stateless)
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // לא שומרים sessions
        );

        // הגדרת הרשאות בגרסה החדשה
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers(
                                "/auth/**",
                                "/api/users/register",
                                "/api/users/login",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/reservation").authenticated()
                        .requestMatchers(HttpMethod.GET, "/reservation/my").authenticated()


        );


        // טיפול בשגיאות אוטנטיקציה
        http.exceptionHandling(exception ->
                exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
        );

        // הוספת ה-JWT Filter לפני ה-UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // יצירת אובייקט passwordEncoder
    }
}
