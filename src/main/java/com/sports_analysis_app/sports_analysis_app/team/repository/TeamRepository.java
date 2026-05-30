package com.sports_analysis_app.sports_analysis_app.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sports_analysis_app.sports_analysis_app.team.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    
    Team findByTeamUidContainingIgnoreCase(String teamUid);
}
