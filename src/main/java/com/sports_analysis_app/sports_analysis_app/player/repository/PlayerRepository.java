package com.sports_analysis_app.sports_analysis_app.player.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sports_analysis_app.sports_analysis_app.player.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByEmail(String email);
        
}
