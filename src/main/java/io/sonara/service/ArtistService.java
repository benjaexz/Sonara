package io.sonara.service;

import io.sonara.dto.ArtistRequestDTO;
import io.sonara.entity.Artist;
import io.sonara.exception.DuplicateResourceException;
import io.sonara.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    public Artist save(ArtistRequestDTO dto) {
        artistRepository.findByName(dto.name())
                .ifPresent(existingArtist -> {
                    throw new DuplicateResourceException("Artist already exists");
                });

        Artist artist = new Artist();
        artist.setName(dto.name());

        return artistRepository.save(artist);
    }

    public List<Artist> findAll() {
        return artistRepository.findAll();
    }
}