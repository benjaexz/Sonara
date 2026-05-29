package io.sonara.service;

import io.sonara.dto.GenreRequestDTO;
import io.sonara.dto.GenreResponseDTO;
import io.sonara.entity.Genre;
import io.sonara.exception.DuplicateResourceException;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public GenreResponseDTO save(GenreRequestDTO dto) {
        genreRepository.findByName(dto.getName())
                .ifPresent(existingGenre -> {
                    throw new DuplicateResourceException("Genre already exists");
                });

        Genre genre = new Genre();
        genre.setName(dto.getName());

        Genre savedGenre = genreRepository.save(genre);

        return toResponseDTO(savedGenre);
    }

    public List<GenreResponseDTO> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public GenreResponseDTO findById(UUID id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));

        return toResponseDTO(genre);
    }
    public GenreResponseDTO update(UUID id, GenreRequestDTO dto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));

        genreRepository.findByName(dto.getName())
                .ifPresent(existingGenre -> {
                    if (!existingGenre.getId().equals(id)) {
                        throw new DuplicateResourceException("Genre already exists");
                    }
                });

        genre.setName(dto.getName());

        Genre updatedGenre = genreRepository.save(genre);

        return toResponseDTO(updatedGenre);
    }

    public void delete(UUID id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));

        genreRepository.delete(genre);
    }


    private GenreResponseDTO toResponseDTO(Genre genre) {
        return new GenreResponseDTO(
                genre.getId(),
                genre.getName()
        );
    }
}