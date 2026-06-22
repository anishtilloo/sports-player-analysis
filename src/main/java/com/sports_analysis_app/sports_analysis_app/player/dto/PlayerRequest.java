package com.sports_analysis_app.sports_analysis_app.player.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PlayerRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Current team name is required")
    private String currentTeamName;

    private Integer jerseyNumber;

    public PlayerRequest() {}

    public PlayerRequest(String name, String email, String role, String currentTeamName, Integer jerseyNumber) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.currentTeamName = currentTeamName;
        this.jerseyNumber = jerseyNumber;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getCurrentTeamName() { return currentTeamName; }
    public void setCurrentTeamName(String currentTeamName) { this.currentTeamName = currentTeamName; }
    public Integer getJerseyNumber() { return jerseyNumber; }
    public void setJerseyNumber(Integer jerseyNumber) { this.jerseyNumber = jerseyNumber; }
}
