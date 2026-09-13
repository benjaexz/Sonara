package io.sonara.service;

import io.sonara.dto.*;
import io.sonara.entity.*;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.ListeningHistoryRepository;
import io.sonara.repository.TrackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListeningHistoryService {

    private final ListeningHistoryRepository listeningHistoryRepository;
    private final TrackRepository trackRepository;

    public ListeningHistoryService(
            ListeningHistoryRepository listeningHistoryRepository,
            TrackRepository trackRepository
    ) {
        this.listeningHistoryRepository = listeningHistoryRepository;
        this.trackRepository = trackRepository;
    }

    public void registerListening(UUID trackId, User user) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        ListeningHistory history = new ListeningHistory(user, track);
        listeningHistoryRepository.save(history);
    }

    public List<ListeningHistoryResponseDTO> getHistory(User user) {
        return listeningHistoryRepository.findByUserOrderByListenedAtDesc(user)
                .stream()
                .map(history -> new ListeningHistoryResponseDTO(
                        history.getId(),
                        mapTrack(history.getTrack()),
                        history.getListenedAt()
                ))
                .toList();
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