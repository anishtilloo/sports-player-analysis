package com.sports_analysis_app.sports_analysis_app.team.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sports_analysis_app.sports_analysis_app.annotation.auth.AuthRequired;
import com.sports_analysis_app.sports_analysis_app.team.entity.Team;
import com.sports_analysis_app.sports_analysis_app.team.service.TeamServices;

@Controller
@RequestMapping("/api/team")
public class TeamController {
    
    @Autowired
    private TeamServices service;

    @AuthRequired
    @GetMapping("/{uid}")
    public Team getTeamByUid(@RequestParam String uid) {
        return service.getTeamRepository(uid);
    }
}
