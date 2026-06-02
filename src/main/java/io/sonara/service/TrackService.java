package io.sonara.service;

import io.sonara.dto.AlbumResponseDTO;
import io.sonara.dto.ArtistResponseDTO;
import io.sonara.dto.GenreResponseDTO;
import io.sonara.dto.TrackRequestDTO;
import io.sonara.dto.TrackResponseDTO;
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

    public TrackResponseDTO createTrack(TrackRequestDTO dto) {

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

        Track savedTrack = trackRepository.save(track);

        return toResponseDTO(savedTrack);
    }

    public List<TrackResponseDTO> getAllTracks() {
        return trackRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public TrackResponseDTO getTrackById(UUID id) {
        Track track = findTrackEntityById(id);

        return toResponseDTO(track);
    }

    public TrackResponseDTO updateTrack(UUID id, TrackRequestDTO dto) {

        Track track = findTrackEntityById(id);

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

        Track updatedTrack = trackRepository.save(track);

        return toResponseDTO(updatedTrack);
    }

    public void deleteTrack(UUID id) {

        Track track = findTrackEntityById(id);

        trackRepository.delete(track);
    }

    private Track findTrackEntityById(UUID id) {
        return trackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));
    }

    public TrackResponseDTO toResponseDTO(Track track) {
        return new TrackResponseDTO(
                track.getId(),
                track.getTitle(),
                track.getDurationSeconds(),
                toArtistResponseDTO(track.getArtist()),
                toAlbumResponseDTO(track.getAlbum()),
                toGenreResponseDTO(track.getGenre())
        );
    }

    private ArtistResponseDTO toArtistResponseDTO(Artist artist) {
        if (artist == null) {
            return null;
        }

        return new ArtistResponseDTO(
                artist.getId(),
                artist.getName()
        );
    }

    private AlbumResponseDTO toAlbumResponseDTO(Album album) {
        if (album == null) {
            return null;
        }

        return new AlbumResponseDTO(
                album.getId(),
                album.getTitle(),
                album.getReleaseYear(),
                toArtistResponseDTO(album.getArtist())
        );
    }

    private GenreResponseDTO toGenreResponseDTO(Genre genre) {
        if (genre == null) {
            return null;
        }

        return new GenreResponseDTO(
                genre.getId(),
                genre.getName()
        );
    }
}