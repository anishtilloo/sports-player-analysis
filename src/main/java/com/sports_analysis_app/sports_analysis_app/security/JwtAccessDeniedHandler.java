package com.sports_analysis_app.sports_analysis_app.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.sports_analysis_app.sports_analysis_app.security.SecurityErrorWriter.HttpServletResponseStatus;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final SecurityErrorWriter securityErrorWriter;

    public JwtAccessDeniedHandler(SecurityErrorWriter securityErrorWriter) {
        this.securityErrorWriter = securityErrorWriter;
    }

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        securityErrorWriter.write(
            request,
            response,
            HttpServletResponseStatus.FORBIDDEN,
            "You do not have permission to access this route"
        );
    }
}
