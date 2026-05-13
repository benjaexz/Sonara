package io.sonara.controller;

import io.sonara.dto.GenreRequestDTO;
import io.sonara.entity.Genre;
import io.sonara.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @PostMapping
    public Genre save(@RequestBody @Valid GenreRequestDTO dto) {
        return genreService.save(dto);
    }

    @GetMapping
    public List<Genre> findAll() {
        return genreService.findAll();
    }
}