package com.sports_analysis_app.sports_analysis_app.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sports_analysis_app.sports_analysis_app.security.SecurityErrorWriter.HttpServletResponseStatus;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final SecurityErrorWriter securityErrorWriter;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, SecurityErrorWriter securityErrorWriter) {
        this.jwtUtil = jwtUtil;
        this.securityErrorWriter = securityErrorWriter;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || authHeader.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!authHeader.startsWith("Bearer ")) {
                writeUnauthorized(request, response, "Authorization header must use the Bearer token format");
                return;
            }

            String token = authHeader.substring(7);

            if (token.isBlank()) {
                writeUnauthorized(request, response, "Bearer token is missing");
                return;
            }

            if (!jwtUtil.validateAccessToken(token)) {
                writeUnauthorized(request, response, "Access token is invalid or expired");
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
               String email = jwtUtil.extractEmailFromAccessToken(token);
            //    Long userId = jwtUtil.extractUserIdFromAccessToken(token);

               Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
               authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

               UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
               authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

               SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            writeUnauthorized(request, response, "Access token could not be processed");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeUnauthorized(
        HttpServletRequest request,
        HttpServletResponse response,
        String message
    ) throws IOException {
        securityErrorWriter.write(request, response, HttpServletResponseStatus.UNAUTHORIZED, message);
    }
}
