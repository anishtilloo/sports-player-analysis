package com.sports_analysis_app.sports_analysis_app.player.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table(name="players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String currentTeamName;

    private Integer jerseyNumber;

    private Integer runsScored = 0;
    private Integer wicketsTaken = 0;
    private Float battingAverage = 0f;
    private Float bolwingAverage = 0f;

    @Column(nullable = false,  name = "created_at")
    private Long createdAt;

    @Column(nullable = false,  name = "updated_at")
    private Long updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getRunsScored() {
        return runsScored;
    }

    public void setRunsScored(Integer runsScored) {
        this.runsScored = runsScored;
    }

    public Integer getWicketsTaken() {
        return wicketsTaken;
    }

    public void setWicketsTaken(Integer wicketsTaken) {
        this.wicketsTaken = wicketsTaken;
    }

    public Float getBattingAverage() {
        return battingAverage;
    }

    public void setBattingAverage(Float battingAverage) {
        this.battingAverage = battingAverage;
    }

    public Float getBolwingAverage() {
        return bolwingAverage;
    }

    public void setBolwingAverage(Float bolwingAverage) {
        this.bolwingAverage = bolwingAverage;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

}
