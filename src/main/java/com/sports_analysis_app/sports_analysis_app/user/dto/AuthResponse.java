package com.sports_analysis_app.sports_analysis_app.user.dto;

public class AuthResponse {
    private Long userId;
    private String token;
    private String email;
    private String message;

    public AuthResponse(Long userId, String token, String email, String message) {
        this.userId = userId;
        this.token = token;
        this.email = email;
        this.message = message;
    }
    
    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
