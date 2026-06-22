package com.sports_analysis_app.sports_analysis_app.organization.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sports_analysis_app.sports_analysis_app.annotation.auth.AuthRequired;
import com.sports_analysis_app.sports_analysis_app.organization.dto.CreateOrganizationRequestDto;
import com.sports_analysis_app.sports_analysis_app.organization.dto.CreateOrganizationResponse;
import com.sports_analysis_app.sports_analysis_app.organization.entity.Organization;
import com.sports_analysis_app.sports_analysis_app.organization.service.OrganizationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    private final OrganizationService service;

    public OrganizationController(OrganizationService service) {
        this.service = service;
    }

    @AuthRequired
    @GetMapping("/{uid}")
    public ResponseEntity<Organization> getOrganizationByUid(@PathVariable String uid) {
        return ResponseEntity.ok(service.getOrganizationByUid(uid));
    }

    @PostMapping
    public ResponseEntity<CreateOrganizationResponse> createOrganization(@Valid @RequestBody CreateOrganizationRequestDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrganization(entity));
    }
}
