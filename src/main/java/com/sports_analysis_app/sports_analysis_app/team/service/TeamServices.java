package com.sports_analysis_app.sports_analysis_app.team.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sports_analysis_app.sports_analysis_app.team.entity.Team;
import com.sports_analysis_app.sports_analysis_app.team.repository.TeamRepository;

@Service
public class TeamServices {
    @Autowired
    private TeamRepository teamRepository;

    public Team getTeamRepository(String teamUid) {
        return teamRepository.findByTeamUidContainingIgnoreCase(teamUid);
    }
}
