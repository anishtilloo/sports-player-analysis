package com.sports_analysis_app.sports_analysis_app.player.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerRequest;
import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerUpdateRequest;
import com.sports_analysis_app.sports_analysis_app.player.entity.Player;
import com.sports_analysis_app.sports_analysis_app.player.repository.PlayerRepository;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Optional<Player> getPlayerById(Long playerId) {
        if (playerId == null) {
            throw new IllegalArgumentException("Please provide appropriate player id");
        }

        Optional<Player> player = playerRepository.findById(playerId);

        if (player == null) {
            throw new IllegalArgumentException("Player with this id does not exist");
        }
        return player;
    }

    public Player getPlayerByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is Required");
        }

        Player player = playerRepository.findByEmail(email);

        if (player == null) {
            throw new IllegalArgumentException("Player with this email does not exist");
        }

        return player;
    }

    public Player registerPlayer(PlayerRequest playerPayload) {
        if (playerPayload.getEmail() == null || playerPayload.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is Required");
        }

        Player existingPlayer = playerRepository.findByEmail(playerPayload.getEmail());

        if (existingPlayer != null) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (playerPayload.getName() == null || playerPayload.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is Required");
        }

        long now = System.currentTimeMillis();
        Player player = new Player(playerPayload, now, now);

        Player savePlayer = playerRepository.save(player);

        return savePlayer;
    }

    public void deletePlayer(Long playerId) {
        if (playerId == null) {
            throw new IllegalArgumentException("Please provide appropriate player id");
        }
        playerRepository.deleteById(playerId);
    }

    public Player getPlayerByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is Required");
        }

        Player player = playerRepository.findByNameContainingIgnoreCases(name);

        if (player == null) {
            throw new IllegalArgumentException("Player with this name does not exist");
        }

        return player;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public List<Player> getPlayersByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role is Required");
        }

        return playerRepository.findByRoleContainingIgnoreCases(role);
    }

    public List<Player> getPlayersByTeam(String team) {
        if (team == null || team.trim().isEmpty()) {
            throw new IllegalArgumentException("Team is Required");
        }
        return playerRepository.findPlayersByTeam(team);
    }

    public List<Player> searchPlayers(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term is Required");
        }

        return playerRepository.findByEmailContainingIgnoreCasesOrNameContainingIgnoreCases(query, query);
    }

    public Player updatePlayer(Long id, PlayerUpdateRequest request) {
        Player existingPlayer = this.getPlayerById(id).orElseThrow(() -> new RuntimeException("Player not found with id" + id));

        existingPlayer.setName(request.getName());
        existingPlayer.setEmail(request.getEmail());
        existingPlayer.setRole(request.getRole());
        existingPlayer.setCurrentTeamName(request.getCurrentTeamName());
        existingPlayer.setJerseyNumber(request.getJerseyNumber());

        Player updatedPlayer = playerRepository.save(existingPlayer);
        return updatedPlayer;
    }
}
