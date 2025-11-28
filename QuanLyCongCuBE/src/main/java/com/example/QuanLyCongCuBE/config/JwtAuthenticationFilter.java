package com.example.QuanLyCongCuBE.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.QuanLyCongCuBE.service.JwtService;
import com.example.QuanLyCongCuBE.service.UserService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String Email;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        Email = jwtService.extractEmail(jwt);

        if (Email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Extracted email from token: " + Email);
        
            try {
                UserDetails userDetails = userService.loadUserByUsername(Email);
                System.out.println("User loaded: " + userDetails.getUsername());
        
                boolean valid = jwtService.isTokenValid(jwt, userDetails.getUsername());
                System.out.println("Token valid: " + valid);
        
                if (valid) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
        
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
        
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication set in SecurityContext for user: " + Email);
                } else {
                    System.out.println("Token invalid for user: " + Email);
                }
        
            } catch (Exception e) {
                System.err.println(" Error while validating JWT for user: " + Email);
                e.printStackTrace();
            }
        } else {
            if (Email == null) {
                System.out.println("No email found in token");
            }
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                System.out.println("User already authenticated, skipping JWT validation");
            }
        }
        filterChain.doFilter(request, response);
    }
}