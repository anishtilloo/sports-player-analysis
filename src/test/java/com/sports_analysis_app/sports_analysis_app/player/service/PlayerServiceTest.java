package com.sports_analysis_app.sports_analysis_app.player.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sports_analysis_app.sports_analysis_app.common.exception.ResourceNotFoundException;
import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerRequest;
import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerUpdateRequest;
import com.sports_analysis_app.sports_analysis_app.player.entity.Player;
import com.sports_analysis_app.sports_analysis_app.player.repository.PlayerRepository;
import com.sports_analysis_app.sports_analysis_app.team.entity.Team;
import com.sports_analysis_app.sports_analysis_app.team.repository.TeamRepository;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock private PlayerRepository playerRepository;
    @Mock private TeamRepository teamRepository;

    @InjectMocks
    private PlayerService playerService;

    private static final String TEAM_UID = "team-uid-123";

    private Team sampleTeam() {
        Team team = new Team();
        team.setTeamUid(TEAM_UID);
        team.setName("Team A");
        return team;
    }

    private Player samplePlayer() {
        PlayerRequest req = new PlayerRequest("John Doe", "john@example.com", "Batsman", TEAM_UID, 10);
        Player player = new Player(req, System.currentTimeMillis(), System.currentTimeMillis());
        player.setTeam(sampleTeam());
        return player;
    }

    @Test
    void getPlayerById_found() {
        Player player = samplePlayer();
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        Player result = playerService.getPlayerById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void getPlayerById_notFound_throwsException() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.getPlayerById(99L));
    }

    @Test
    void getPlayerById_nullId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> playerService.getPlayerById(null));
    }

    @Test
    void registerPlayer_success() {
        PlayerRequest req = new PlayerRequest("Jane Doe", "jane@example.com", "Bowler", TEAM_UID, 7);
        when(playerRepository.findByEmail("jane@example.com")).thenReturn(null);
        when(teamRepository.findByTeamUidContainingIgnoreCase(TEAM_UID)).thenReturn(sampleTeam());
        when(playerRepository.save(any(Player.class))).thenAnswer(i -> i.getArgument(0));

        Player result = playerService.registerPlayer(req);

        assertNotNull(result);
        assertEquals("jane@example.com", result.getEmail());
        assertNotNull(result.getTeam());
    }

    @Test
    void registerPlayer_emailTaken_throwsException() {
        PlayerRequest req = new PlayerRequest("Jane Doe", "jane@example.com", "Bowler", TEAM_UID, 7);
        when(playerRepository.findByEmail("jane@example.com")).thenReturn(samplePlayer());

        assertThrows(IllegalArgumentException.class, () -> playerService.registerPlayer(req));
    }

    @Test
    void registerPlayer_teamNotFound_throwsException() {
        PlayerRequest req = new PlayerRequest("Jane Doe", "jane@example.com", "Bowler", "bad-uid", 7);
        when(playerRepository.findByEmail("jane@example.com")).thenReturn(null);
        when(teamRepository.findByTeamUidContainingIgnoreCase("bad-uid")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> playerService.registerPlayer(req));
    }

    @Test
    void deletePlayer_notFound_throwsException() {
        when(playerRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> playerService.deletePlayer(99L));
    }

    @Test
    void deletePlayer_success() {
        when(playerRepository.existsById(1L)).thenReturn(true);

        playerService.deletePlayer(1L);

        verify(playerRepository).deleteById(1L);
    }

    @Test
    void getPlayersByRole_blankRole_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> playerService.getPlayersByRole(""));
    }

    @Test
    void searchPlayers_blankQuery_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> playerService.searchPlayers(""));
    }

    @Test
    void getAllPlayers_returnsList() {
        when(playerRepository.findAll()).thenReturn(List.of(samplePlayer()));

        List<Player> result = playerService.getAllPlayers();

        assertEquals(1, result.size());
    }

    @Test
    void updatePlayer_notFound_throwsException() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());
        PlayerUpdateRequest req = new PlayerUpdateRequest("X", "x@x.com", "Batsman", TEAM_UID, 1);

        assertThrows(ResourceNotFoundException.class, () -> playerService.updatePlayer(99L, req));
    }
}
