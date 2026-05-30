package com.sports_analysis_app.sports_analysis_app.organization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sports_analysis_app.sports_analysis_app.organization.entity.Organization;
import com.sports_analysis_app.sports_analysis_app.organization.repository.OrganizationRepository;

@Service
public class OrganizationService {
    
    @Autowired
    private OrganizationRepository repository;

    public Organization getOrganizationByUid(String uid) {
        return repository.findByUidContainingIgnoreCase(uid);
    }
}
