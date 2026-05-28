package io.sonara.controller;

import io.sonara.dto.GenreRequestDTO;
import io.sonara.dto.GenreResponseDTO;
import io.sonara.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping
    public ResponseEntity<GenreResponseDTO> save(@RequestBody @Valid GenreRequestDTO dto) {
        GenreResponseDTO createdGenre = genreService.save(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdGenre);
    }

    @GetMapping
    public ResponseEntity<List<GenreResponseDTO>> findAll() {
        return ResponseEntity.ok(genreService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(genreService.findById(id));
    }
}