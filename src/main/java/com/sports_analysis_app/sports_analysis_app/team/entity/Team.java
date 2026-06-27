package com.sports_analysis_app.sports_analysis_app.team.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sports_analysis_app.sports_analysis_app.organization.entity.Organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="teams")
@EntityListeners(AuditingEntityListener.class)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "team_uid")
    private String teamUid;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "org_id", referencedColumnName = "id", nullable = false)
    private Organization organization;

    @CreatedDate
    @Column(nullable = false,  name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false,  name = "updated_at")
    private Instant updatedAt;

    public Team() {}

    public Team(String teamUid, String name, Organization organization, Instant createdAt, Instant updatedAt) {
        this.teamUid = teamUid;
        this.name = name;
        this.organization = organization;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getTeamUid() {
        return teamUid;
    }

    public void setTeamUid(String teamUid) {
        this.teamUid = teamUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}