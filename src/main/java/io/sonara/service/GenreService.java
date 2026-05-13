package io.sonara.service;

import io.sonara.entity.Genre;
import io.sonara.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }
}