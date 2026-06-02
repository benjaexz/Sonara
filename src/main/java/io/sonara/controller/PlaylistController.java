package io.sonara.controller;

import io.sonara.dto.PlaylistRequestDTO;
import io.sonara.dto.PlaylistResponseDTO;
import io.sonara.entity.User;
import io.sonara.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistResponseDTO createPlaylist(
            @RequestBody @Valid PlaylistRequestDTO request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        return playlistService.createPlaylist(
                user.getEmail(),
                request
        );
    }

    @GetMapping
    public List<PlaylistResponseDTO> listPlaylists(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        return playlistService.listPlaylists(
                user.getEmail()
        );
    }

    @PostMapping("/{playlistId}/tracks/{trackId}")
    public PlaylistResponseDTO addTrackToPlaylist(
            @PathVariable UUID playlistId,
            @PathVariable UUID trackId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        return playlistService.addTrackToPlaylist(
                playlistId,
                trackId,
                user.getEmail()
        );
    }

    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    public PlaylistResponseDTO removeTrackFromPlaylist(
            @PathVariable UUID playlistId,
            @PathVariable UUID trackId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        return playlistService.removeTrackFromPlaylist(
                playlistId,
                trackId,
                user.getEmail()
        );
    }
}