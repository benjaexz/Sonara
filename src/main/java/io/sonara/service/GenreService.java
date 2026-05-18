package io.sonara.service;

import io.sonara.dto.GenreRequestDTO;
import io.sonara.entity.Genre;
import io.sonara.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public Genre save(GenreRequestDTO dto) {
        genreRepository.findByName(dto.getName())
                .ifPresent(existingGenre -> {
                    throw new RuntimeException("Genre already exists");
                });

        Genre genre = new Genre();

        genre.setName(dto.getName());

        return genreRepository.save(genre);
    }

}