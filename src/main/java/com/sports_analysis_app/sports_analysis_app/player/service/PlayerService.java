package com.sports_analysis_app.sports_analysis_app.player.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sports_analysis_app.sports_analysis_app.common.exception.ResourceNotFoundException;
import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerRequest;
import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerUpdateRequest;
import com.sports_analysis_app.sports_analysis_app.player.entity.Player;
import com.sports_analysis_app.sports_analysis_app.player.repository.PlayerRepository;
import com.sports_analysis_app.sports_analysis_app.team.entity.Team;
import com.sports_analysis_app.sports_analysis_app.team.repository.TeamRepository;

@Service
public class PlayerService {
    private static final Logger log = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    public Player getPlayerById(Long playerId) {
        if (playerId == null) {
            throw new IllegalArgumentException("Player id is required");
        }
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + playerId));
    }

    public Player getPlayerByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        Player player = playerRepository.findByEmail(email);
        if (player == null) {
            throw new ResourceNotFoundException("Player not found with email: " + email);
        }
        return player;
    }

    public Player registerPlayer(PlayerRequest playerPayload) {
        Player existingPlayer = playerRepository.findByEmail(playerPayload.getEmail());
        if (existingPlayer != null) {
            throw new IllegalArgumentException("Email already registered");
        }

        Team team = teamRepository.findByTeamUidContainingIgnoreCase(playerPayload.getTeamUid());
        if (team == null) {
            throw new ResourceNotFoundException("Team not found with uid: " + playerPayload.getTeamUid());
        }

        long now = System.currentTimeMillis();
        Player player = new Player(playerPayload, now, now);
        player.setPlayerUid(UUID.randomUUID().toString());
        player.setTeam(team);
        Player savedPlayer = playerRepository.save(player);

        log.info("Registered new player with email: {}", playerPayload.getEmail());
        return savedPlayer;
    }

    public void deletePlayer(Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new ResourceNotFoundException("Player not found with id: " + playerId);
        }
        playerRepository.deleteById(playerId);
        log.info("Deleted player with id: {}", playerId);
    }

    public Player getPlayerByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        Player player = playerRepository.findByNameContainingIgnoreCase(name);
        if (player == null) {
            throw new ResourceNotFoundException("Player not found with name: " + name);
        }
        return player;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public List<Player> getPlayersByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }
        return playerRepository.findAllByRoleContainingIgnoreCase(role);
    }

    public List<Player> getPlayersByTeamUid(String teamUid) {
        if (teamUid == null || teamUid.trim().isEmpty()) {
            throw new IllegalArgumentException("Team UID is required");
        }
        return playerRepository.findAllByTeam_TeamUid(teamUid);
    }

    public List<Player> searchPlayers(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term is required");
        }
        return playerRepository.findAllByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(query, query);
    }

    public Player updatePlayer(Long id, PlayerUpdateRequest request) {
        Player existingPlayer = getPlayerById(id);

        Team team = teamRepository.findByTeamUidContainingIgnoreCase(request.getTeamUid());
        if (team == null) {
            throw new ResourceNotFoundException("Team not found with uid: " + request.getTeamUid());
        }

        existingPlayer.setName(request.getName());
        existingPlayer.setEmail(request.getEmail());
        existingPlayer.setRole(request.getRole());
        existingPlayer.setTeam(team);
        existingPlayer.setJerseyNumber(request.getJerseyNumber());
        Player updated = playerRepository.save(existingPlayer);
        log.info("Updated player with id: {}", id);
        return updated;
    }
}
