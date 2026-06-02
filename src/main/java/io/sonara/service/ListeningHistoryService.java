package io.sonara.service;

import io.sonara.dto.*;
import io.sonara.entity.*;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.ListeningHistoryRepository;
import io.sonara.repository.TrackRepository;
import io.sonara.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListeningHistoryService {

    private final ListeningHistoryRepository listeningHistoryRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;

    public ListeningHistoryService(
            ListeningHistoryRepository listeningHistoryRepository,
            TrackRepository trackRepository,
            UserRepository userRepository
    ) {
        this.listeningHistoryRepository = listeningHistoryRepository;
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
    }

    public void registerListening(UUID trackId, Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        ListeningHistory history = new ListeningHistory(user, track);

        listeningHistoryRepository.save(history);
    }

    public List<ListeningHistoryResponseDTO> getHistory(Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        return listeningHistoryRepository.findByUserOrderByListenedAtDesc(user)
                .stream()
                .map(history -> new ListeningHistoryResponseDTO(
                        history.getId(),
                        mapTrack(history.getTrack()),
                        history.getListenedAt()
                ))
                .toList();
    }

    private User getAuthenticatedUser(Authentication authentication) {

        if (authentication == null || authentication.getName() == null) {
            throw new ResourceNotFoundException("Authenticated user not found");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private TrackResponseDTO mapTrack(Track track) {

        return new TrackResponseDTO(
                track.getId(),
                track.getTitle(),
                track.getDurationSeconds(),
                new ArtistResponseDTO(
                        track.getArtist().getId(),
                        track.getArtist().getName()
                ),
                new AlbumResponseDTO(
                        track.getAlbum().getId(),
                        track.getAlbum().getTitle(),
                        track.getAlbum().getReleaseYear(),
                        new ArtistResponseDTO(
                                track.getAlbum().getArtist().getId(),
                                track.getAlbum().getArtist().getName()
                        )
                ),
                new GenreResponseDTO(
                        track.getGenre().getId(),
                        track.getGenre().getName()
                )
        );
    }
}