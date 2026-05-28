package io.sonara.service;

import io.sonara.dto.AlbumRequestDTO;
import io.sonara.dto.AlbumResponseDTO;
import io.sonara.dto.ArtistResponseDTO;
import io.sonara.entity.Album;
import io.sonara.entity.Artist;
import io.sonara.exception.DuplicateResourceException;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.AlbumRepository;
import io.sonara.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public AlbumService(
            AlbumRepository albumRepository,
            ArtistRepository artistRepository
    ) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    public AlbumResponseDTO save(AlbumRequestDTO dto) {

        albumRepository.findByTitle(dto.title())
                .ifPresent(existingAlbum -> {
                    throw new DuplicateResourceException("Album already exists");
                });

        Artist artist = artistRepository.findById(dto.artistId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Artist not found"));

        Album album = new Album();

        album.setTitle(dto.title());
        album.setReleaseYear(dto.releaseYear());
        album.setArtist(artist);

        Album savedAlbum = albumRepository.save(album);

        return toResponseDTO(savedAlbum);
    }

    public List<AlbumResponseDTO> findAll() {
        return albumRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public AlbumResponseDTO findById(UUID id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));

        return toResponseDTO(album);
    }

    private AlbumResponseDTO toResponseDTO(Album album) {
        Artist artist = album.getArtist();

        ArtistResponseDTO artistResponseDTO = new ArtistResponseDTO(
                artist.getId(),
                artist.getName()
        );

        return new AlbumResponseDTO(
                album.getId(),
                album.getTitle(),
                album.getReleaseYear(),
                artistResponseDTO
        );
    }
}