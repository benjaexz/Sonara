package io.sonara.controller;

import io.sonara.dto.ArtistRequestDTO;
import io.sonara.entity.Artist;
import io.sonara.service.ArtistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @PostMapping
    public Artist save(@RequestBody @Valid ArtistRequestDTO dto) {
        return artistService.save(dto);
    }

    @GetMapping
    public List<Artist> findAll() {
        return artistService.findAll();
    }
}