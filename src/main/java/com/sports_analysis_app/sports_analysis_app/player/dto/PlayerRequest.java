package com.sports_analysis_app.sports_analysis_app.player.dto;

public class PlayerRequest {
    private String name;
    private String email;
    private String role;
    private String currentTeamName;
    private Integer jerseyNumber;

    public PlayerRequest(String name, String email, String role, String currentTeamName, Integer jerseyNumber) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.currentTeamName = currentTeamName;
        this.jerseyNumber = jerseyNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCurrentTeamName() {
        return currentTeamName;
    }

    public void setCurrentTeamName(String currentTeamName) {
        this.currentTeamName = currentTeamName;
    }

    public Integer getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(Integer jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    
}
