package io.sonara.service;

import org.springframework.transaction.annotation.Transactional;
import io.sonara.dto.PlaylistRequestDTO;
import io.sonara.dto.PlaylistResponseDTO;
import io.sonara.dto.PlaylistTrackResponseDTO;
import io.sonara.entity.Playlist;
import io.sonara.entity.PlaylistTrack;
import io.sonara.entity.Track;
import io.sonara.entity.User;
import io.sonara.exception.DuplicateResourceException;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.PlaylistRepository;
import io.sonara.repository.PlaylistTrackRepository;
import io.sonara.repository.TrackRepository;
import io.sonara.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    private final TrackService trackService;

    public PlaylistService(
            PlaylistRepository playlistRepository,
            PlaylistTrackRepository playlistTrackRepository,
            TrackRepository trackRepository,
            UserRepository userRepository,
            TrackService trackService
    ) {
        this.playlistRepository = playlistRepository;
        this.playlistTrackRepository = playlistTrackRepository;
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
        this.trackService = trackService;
    }

    public PlaylistResponseDTO createPlaylist(
            String email,
            PlaylistRequestDTO request
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (playlistRepository.existsByUserAndName(user, request.name())) {
            throw new DuplicateResourceException("Playlist already exists");
        }

        Playlist playlist = new Playlist(
                request.name(),
                user
        );

        Playlist savedPlaylist = playlistRepository.save(playlist);

        return toResponseDTO(savedPlaylist);
    }

    public List<PlaylistResponseDTO> listPlaylists(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return playlistRepository.findByUser(user)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public PlaylistResponseDTO addTrackToPlaylist(
            UUID playlistId,
            UUID trackId,
            String email
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        if (!playlist.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Playlist not found");
        }

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        if (playlistTrackRepository.existsByPlaylistAndTrack(playlist, track)) {
            throw new DuplicateResourceException("Track already exists in playlist");
        }

        int nextPosition = playlistTrackRepository
                .findByPlaylistOrderByPositionAsc(playlist)
                .size() + 1;

        PlaylistTrack playlistTrack = new PlaylistTrack(
                playlist,
                track,
                nextPosition
        );

        playlistTrackRepository.save(playlistTrack);

        return toResponseDTO(playlist);
    }
    @Transactional
    public PlaylistResponseDTO removeTrackFromPlaylist(
            UUID playlistId,
            UUID trackId,
            String email
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        if (!playlist.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Playlist not found");
        }

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        if (!playlistTrackRepository.existsByPlaylistAndTrack(playlist, track)) {
            throw new ResourceNotFoundException("Track not found in playlist");
        }

        playlistTrackRepository.deleteByPlaylistAndTrack(playlist, track);

        return toResponseDTO(playlist);
    }

    private PlaylistResponseDTO toResponseDTO(Playlist playlist) {
        List<PlaylistTrackResponseDTO> tracks = playlistTrackRepository
                .findByPlaylistOrderByPositionAsc(playlist)
                .stream()
                .map(this::toPlaylistTrackResponseDTO)
                .toList();

        return new PlaylistResponseDTO(
                playlist.getId(),
                playlist.getName(),
                tracks
        );
    }

    private PlaylistTrackResponseDTO toPlaylistTrackResponseDTO(PlaylistTrack playlistTrack) {
        return new PlaylistTrackResponseDTO(
                playlistTrack.getId(),
                trackService.toResponseDTO(playlistTrack.getTrack()),
                playlistTrack.getPosition()
        );
    }
}