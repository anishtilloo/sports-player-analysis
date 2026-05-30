package com.sports_analysis_app.sports_analysis_app.organization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sports_analysis_app.sports_analysis_app.annotation.auth.AuthRequired;
import com.sports_analysis_app.sports_analysis_app.organization.service.OrganizationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/api/organization")
public class OrganizationController {
    
    @Autowired
    private OrganizationService service;

    @AuthRequired
    @GetMapping("/{uid}")
    public String getOrganizationByUid(@RequestParam String uid) {
        return service.getOrganizationByUid(uid).toString();
    }
}
