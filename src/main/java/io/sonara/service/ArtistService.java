package io.sonara.service;

import io.sonara.dto.ArtistRequestDTO;
import io.sonara.dto.ArtistResponseDTO;
import io.sonara.entity.Artist;
import io.sonara.exception.DuplicateResourceException;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public ArtistResponseDTO save(ArtistRequestDTO dto) {
        artistRepository.findByName(dto.name())
                .ifPresent(existingArtist -> {
                    throw new DuplicateResourceException("Artist already exists");
                });

        Artist artist = new Artist();
        artist.setName(dto.name());

        Artist savedArtist = artistRepository.save(artist);

        return toResponseDTO(savedArtist);
    }

    public List<ArtistResponseDTO> findAll() {
        return artistRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ArtistResponseDTO findById(UUID id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        return toResponseDTO(artist);
    }

    private ArtistResponseDTO toResponseDTO(Artist artist) {
        return new ArtistResponseDTO(
                artist.getId(),
                artist.getName()
        );
    }
}