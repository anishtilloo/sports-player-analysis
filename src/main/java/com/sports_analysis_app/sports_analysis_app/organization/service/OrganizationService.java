package com.sports_analysis_app.sports_analysis_app.organization.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sports_analysis_app.sports_analysis_app.organization.dto.CreateOrganizationRequestDto;
import com.sports_analysis_app.sports_analysis_app.organization.dto.CreateOrganizationResponse;
import com.sports_analysis_app.sports_analysis_app.organization.entity.Organization;
import com.sports_analysis_app.sports_analysis_app.organization.repository.OrganizationRepository;
import com.sports_analysis_app.sports_analysis_app.user.dto.AuthResponse;
import com.sports_analysis_app.sports_analysis_app.user.entity.User;
import com.sports_analysis_app.sports_analysis_app.user.service.AuthService;
import com.sports_analysis_app.sports_analysis_app.user.service.UserService;

@Service
public class OrganizationService {
    
    @Autowired
    private OrganizationRepository repository;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    public Organization getOrganizationByUid(String uid) {
        return repository.findByUidContainingIgnoreCase(uid);
    }

    public CreateOrganizationResponse createOrganization(CreateOrganizationRequestDto entity) {
        Instant now = Instant.now();
        Organization organization = new Organization();
        organization.setName(entity.getName());
        organization.setAddress(entity.getAddress());
        organization.setCreatedAt(now);
        organization.setUpdatedAt(now);
        organization.setOrgUid(java.util.UUID.randomUUID().toString());
        AuthResponse authResponse = authService.registerUser(entity.getEmail(), entity.getUserName(), entity.getPassword());
        User user = userService.getUserByEmail(authResponse.getEmail());
        organization.setUser(user);
        repository.save(organization);
        return new CreateOrganizationResponse(organization.getOrgUid(), organization.getName());
    }
}
