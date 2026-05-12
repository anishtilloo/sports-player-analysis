package com.sports_analysis_app.sports_analysis_app.player.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerRequest;
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
        if (email == null) {
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
}
