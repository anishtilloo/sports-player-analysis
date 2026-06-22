package com.sports_analysis_app.sports_analysis_app.organization.service;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sports_analysis_app.sports_analysis_app.common.exception.ResourceNotFoundException;
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
    private static final Logger log = LoggerFactory.getLogger(OrganizationService.class);

    private final OrganizationRepository repository;
    private final AuthService authService;
    private final UserService userService;

    public OrganizationService(OrganizationRepository repository, AuthService authService, UserService userService) {
        this.repository = repository;
        this.authService = authService;
        this.userService = userService;
    }

    public Organization getOrganizationByUid(String uid) {
        Organization org = repository.findByOrgUidContainingIgnoreCase(uid);
        if (org == null) {
            throw new ResourceNotFoundException("Organization not found with uid: " + uid);
        }
        return org;
    }

    @Transactional
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

        log.info("Created organization '{}' for user: {}", entity.getName(), entity.getEmail());
        return new CreateOrganizationResponse(organization.getOrgUid(), organization.getName());
    }
}
