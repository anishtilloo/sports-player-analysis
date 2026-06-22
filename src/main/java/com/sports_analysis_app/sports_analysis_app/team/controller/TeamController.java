package com.sports_analysis_app.sports_analysis_app.team.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sports_analysis_app.sports_analysis_app.annotation.auth.AuthRequired;
import com.sports_analysis_app.sports_analysis_app.team.entity.Team;
import com.sports_analysis_app.sports_analysis_app.team.service.TeamServices;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamServices service;

    public TeamController(TeamServices service) {
        this.service = service;
    }

    @AuthRequired
    @GetMapping("/{uid}")
    public ResponseEntity<Team> getTeamByUid(@PathVariable String uid) {
        return ResponseEntity.ok(service.getTeamRepository(uid));
    }
}
