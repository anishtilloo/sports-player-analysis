package com.sports_analysis_app.sports_analysis_app.team.service;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sports_analysis_app.sports_analysis_app.common.exception.ResourceNotFoundException;
import com.sports_analysis_app.sports_analysis_app.organization.entity.Organization;
import com.sports_analysis_app.sports_analysis_app.organization.repository.OrganizationRepository;
import com.sports_analysis_app.sports_analysis_app.team.dto.TeamRequest;
import com.sports_analysis_app.sports_analysis_app.team.entity.Team;
import com.sports_analysis_app.sports_analysis_app.team.repository.TeamRepository;

@Service
public class TeamServices {
    private static final Logger log = LoggerFactory.getLogger(TeamServices.class);

    private final TeamRepository teamRepository;
    private final OrganizationRepository organizationRepository;

    public TeamServices(TeamRepository teamRepository, OrganizationRepository organizationRepository) {
        this.teamRepository = teamRepository;
        this.organizationRepository = organizationRepository;
    }

    public Team getTeamByUid(String teamUid) {
        Team team = teamRepository.findByTeamUidContainingIgnoreCase(teamUid);
        if (team == null) {
            throw new ResourceNotFoundException("Team not found with uid: " + teamUid);
        }
        log.debug("Fetched team with uid: {}", teamUid);
        return team;
    }

    public Team createTeam(TeamRequest request) {
        Organization org = organizationRepository.findByOrgUidContainingIgnoreCase(request.getOrgUid());
        if (org == null) {
            throw new ResourceNotFoundException("Organization not found with uid: " + request.getOrgUid());
        }

        Instant now = Instant.now();
        Team team = new Team(UUID.randomUUID().toString(), request.getName(), org, now, now);
        Team saved = teamRepository.save(team);
        log.info("Created team '{}' under org: {}", request.getName(), request.getOrgUid());
        return saved;
    }
}
