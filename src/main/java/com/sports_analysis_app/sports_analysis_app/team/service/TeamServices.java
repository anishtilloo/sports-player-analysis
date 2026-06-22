package com.sports_analysis_app.sports_analysis_app.team.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sports_analysis_app.sports_analysis_app.common.exception.ResourceNotFoundException;
import com.sports_analysis_app.sports_analysis_app.team.entity.Team;
import com.sports_analysis_app.sports_analysis_app.team.repository.TeamRepository;

@Service
public class TeamServices {
    private static final Logger log = LoggerFactory.getLogger(TeamServices.class);

    private final TeamRepository teamRepository;

    public TeamServices(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team getTeamRepository(String teamUid) {
        Team team = teamRepository.findByTeamUidContainingIgnoreCase(teamUid);
        if (team == null) {
            throw new ResourceNotFoundException("Team not found with uid: " + teamUid);
        }
        log.debug("Fetched team with uid: {}", teamUid);
        return team;
    }
}
