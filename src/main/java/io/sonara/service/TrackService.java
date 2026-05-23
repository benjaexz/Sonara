package io.sonara.service;

import io.sonara.dto.TrackRequestDTO;
import io.sonara.entity.Album;
import io.sonara.entity.Artist;
import io.sonara.entity.Genre;
import io.sonara.entity.Track;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.AlbumRepository;
import io.sonara.repository.ArtistRepository;
import io.sonara.repository.GenreRepository;
import io.sonara.repository.TrackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;

    public TrackService(
            TrackRepository trackRepository,
            ArtistRepository artistRepository,
            AlbumRepository albumRepository,
            GenreRepository genreRepository
    ) {
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.genreRepository = genreRepository;
    }

    public Track createTrack(TrackRequestDTO dto) {

        Artist artist = artistRepository.findById(dto.getArtistId())
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        Album album = null;

        if (dto.getAlbumId() != null) {
            album = albumRepository.findById(dto.getAlbumId())
                    .orElseThrow(() -> new ResourceNotFoundException("Album not found"));
        }

        Genre genre = null;

        if (dto.getGenreId() != null) {
            genre = genreRepository.findById(dto.getGenreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
        }

        Track track = new Track(
                dto.getTitle(),
                dto.getDurationSeconds(),
                artist,
                album,
                genre
        );

        return trackRepository.save(track);
    }

    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }

    public Track getTrackById(UUID id) {
        return trackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));
    }

    public Track updateTrack(UUID id, TrackRequestDTO dto) {

        Track track = getTrackById(id);

        Artist artist = artistRepository.findById(dto.getArtistId())
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        Album album = null;

        if (dto.getAlbumId() != null) {
            album = albumRepository.findById(dto.getAlbumId())
                    .orElseThrow(() -> new ResourceNotFoundException("Album not found"));
        }

        Genre genre = null;

        if (dto.getGenreId() != null) {
            genre = genreRepository.findById(dto.getGenreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
        }

        track.setTitle(dto.getTitle());
        track.setDurationSeconds(dto.getDurationSeconds());
        track.setArtist(artist);
        track.setAlbum(album);
        track.setGenre(genre);

        return trackRepository.save(track);
    }

    public void deleteTrack(UUID id) {

        Track track = getTrackById(id);

        trackRepository.delete(track);
    }
}