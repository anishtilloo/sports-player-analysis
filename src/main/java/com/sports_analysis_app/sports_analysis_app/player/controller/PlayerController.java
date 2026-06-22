package com.sports_analysis_app.sports_analysis_app.player.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sports_analysis_app.sports_analysis_app.annotation.auth.AuthRequired;
import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerRequest;
import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerUpdateRequest;
import com.sports_analysis_app.sports_analysis_app.player.entity.Player;
import com.sports_analysis_app.sports_analysis_app.player.service.PlayerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService service;

    public PlayerController(PlayerService service) {
        this.service = service;
    }

    @AuthRequired
    @PostMapping
    public ResponseEntity<Player> createPlayer(@Valid @RequestBody PlayerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerPlayer(request));
    }

    @AuthRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        service.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @AuthRequired
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPlayerById(id));
    }

    @AuthRequired
    @GetMapping("/email/{email}")
    public ResponseEntity<Player> getPlayerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getPlayerByEmail(email));
    }

    @AuthRequired
    @GetMapping("/name/{name}")
    public ResponseEntity<Player> getPlayerByName(@PathVariable String name) {
        return ResponseEntity.ok(service.getPlayerByName(name));
    }

    @AuthRequired
    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(service.getAllPlayers());
    }

    @AuthRequired
    @GetMapping("/role/{role}")
    public ResponseEntity<List<Player>> getPlayersByRole(@PathVariable String role) {
        return ResponseEntity.ok(service.getPlayersByRole(role));
    }

    @AuthRequired
    @GetMapping("/team/{team}")
    public ResponseEntity<List<Player>> getPlayersByTeam(@PathVariable String team) {
        return ResponseEntity.ok(service.getPlayersByTeam(team));
    }

    @AuthRequired
    @GetMapping("/search")
    public ResponseEntity<List<Player>> searchPlayers(@RequestParam String q) {
        return ResponseEntity.ok(service.searchPlayers(q));
    }

    @AuthRequired
    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @Valid @RequestBody PlayerUpdateRequest request) {
        return ResponseEntity.ok(service.updatePlayer(id, request));
    }
}
