package io.sonara.service;

import io.sonara.dto.AlbumRequestDTO;
import io.sonara.entity.Album;
import io.sonara.entity.Artist;
import io.sonara.exception.DuplicateResourceException;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.AlbumRepository;
import io.sonara.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    public Album save(AlbumRequestDTO dto) {

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

        return albumRepository.save(album);
    }

    public List<Album> findAll() {
        return albumRepository.findAll();
    }
}