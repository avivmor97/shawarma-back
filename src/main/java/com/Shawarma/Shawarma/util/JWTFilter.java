package com.Shawarma.Shawarma.util;

import com.Shawarma.Shawarma.service.CustomUserDetailsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("➡️ Request path: " + path);

        // ⛔ מדלג רק על ראוטים ציבוריים
        if (path.equals("/auth/login") || path.equals("/auth/register") ||
                path.startsWith("/swagger") || path.startsWith("/v3")) {
            System.out.println("🛑 Bypassing JWT Filter for path: " + path);
            chain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        System.out.println("🔐 Authorization header: " + authHeader);

        String username = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
            System.out.println("👤 Extracted username from token: " + username);
        } else {
            System.out.println("❌ Missing or malformed Authorization header");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("🔎 Loaded user from DB: " + userDetails.getUsername());

            // ✅ תיקון חשוב: validateToken עם userDetails
            if (jwtUtil.validateToken(token, userDetails)) {
                System.out.println("✅ Token is valid for user: " + username);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("✅✅ Authentication saved in SecurityContext for: " + username);
            } else {
                System.out.println("❌ Token validation failed");
            }
        }

        chain.doFilter(request, response);
    }
}
