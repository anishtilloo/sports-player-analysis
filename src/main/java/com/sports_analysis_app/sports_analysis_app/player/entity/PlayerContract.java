package com.sports_analysis_app.sports_analysis_app.player.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.sports_analysis_app.sports_analysis_app.organization.entity.Organization;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="player_contracts")
public class PlayerContract {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    @Column(nullable = false)
    private Player player;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    @Column(nullable = false)
    private Organization organization;

    @Column(nullable = false,  name = "starts_at")
    private Instant startsAt;

    @Column(nullable = false,  name = "ends_at")
    private Instant endsAt;

    @CreatedDate
    @Column(nullable = false,  name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false,  name = "updated_at")
    private Instant updatedAt;
}
