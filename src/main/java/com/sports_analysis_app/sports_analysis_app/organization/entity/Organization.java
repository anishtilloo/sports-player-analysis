package com.sports_analysis_app.sports_analysis_app.organization.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sports_analysis_app.sports_analysis_app.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="organizations")
@EntityListeners(AuditingEntityListener.class)
public class Organization {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "org_uid")
    private String orgUid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @CreatedDate
    @Column(nullable = false,  name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false,  name = "updated_at")
    private Instant updatedAt;

    @OneToOne(mappedBy = "organization")
    private User user;

    public Organization() {}

    public Organization(String orgUid, String name, String address, Instant createdAt, Instant updatedAt, User user) {
        this.orgUid = orgUid;
        this.name = name;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
    }

    public String getOrgUid() {
        return orgUid;
    }

    public void setOrgUid(String orgUid) {
        this.orgUid = orgUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
