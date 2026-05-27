package com.sports_analysis_app.sports_analysis_app.user.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sports_analysis_app.sports_analysis_app.common.enums.Roles;
import com.sports_analysis_app.sports_analysis_app.organization.entity.Organization;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String uid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Roles role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    @Column(nullable = true)
    private Organization organization;

    @CreatedDate
    @Column(nullable = false,  name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false,  name = "updated_at")
    private Instant updatedAt;

    // When we use a parameterized constructor Hibernate stops using the default constructor so we have to specifically define it
    public User() {}

    public User(String uid, String name, String email, String password, Instant createdAt, Instant updatedAt) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }


    @Override
    public String toString() {
        return "User {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
