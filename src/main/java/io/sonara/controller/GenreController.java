package io.sonara.controller;

import io.sonara.entity.Genre;
import io.sonara.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @PostMapping
    public Genre save(@RequestBody Genre genre) {
        return genreService.save(genre);
    }

    @GetMapping
    public List<Genre> findAll() {
        return genreService.findAll();
    }
}