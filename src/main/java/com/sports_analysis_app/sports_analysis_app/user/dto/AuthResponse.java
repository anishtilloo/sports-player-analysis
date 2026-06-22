package com.sports_analysis_app.sports_analysis_app.user.dto;

public class AuthResponse {
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private String email;
    private String message;

    public AuthResponse() {}

    public AuthResponse(Long userId, String accessToken, String refreshToken, String email, String message) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.message = message;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
