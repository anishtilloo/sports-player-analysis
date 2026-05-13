package com.sports_analysis_app.sports_analysis_app.player.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerRequest;
import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerUpdateRequest;
import com.sports_analysis_app.sports_analysis_app.player.entity.Player;
import com.sports_analysis_app.sports_analysis_app.player.service.PlayerService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/player")
public class PlayerController {
    
    @Autowired
    private PlayerService service;

    @PostMapping
    public Player create(@RequestBody PlayerRequest request) {
       return service.registerPlayer(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletePlayer(id);
    }
    
    @GetMapping("/{id}")
    public Optional<Player> getUser(@PathVariable Long id) {
        return service.getPlayerById(id);
    }

    @GetMapping("/email/{email}")
    public Player getUser(@PathVariable String email) {
        return service.getPlayerByEmail(email);
    }
    
    @GetMapping("/name/{name}")
    public Player getMethodName(@PathVariable String name) {
        return service.getPlayerByName(name);
    }
    
    @GetMapping
    public List<Player> getAllPlayers() {
        return service.getAllPlayers();
    }

    @GetMapping("/role/{role}")
    public List<Player> getPlayersByRole(@PathVariable String role) {
        return service.getPlayersByRole(role);
    }
    
    @GetMapping("/team/{team}")
    public List<Player> getPlayersByTeam(@PathVariable String team) {
        return service.getPlayersByTeam(team);
    }

    @GetMapping("/search")
    public List<Player> searchPlayers(@PathVariable String searchQuery) {
        return service.searchPlayers(searchQuery);
    }
    
    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody PlayerUpdateRequest request) {
        return service.updatePlayer(id, request);
    }
}
