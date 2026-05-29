package io.sonara.controller;

import io.sonara.dto.ArtistRequestDTO;
import io.sonara.dto.ArtistResponseDTO;
import io.sonara.service.ArtistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @PostMapping
    public ResponseEntity<ArtistResponseDTO> save(@RequestBody @Valid ArtistRequestDTO dto) {
        ArtistResponseDTO createdArtist = artistService.save(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdArtist);
    }

    @GetMapping
    public ResponseEntity<List<ArtistResponseDTO>> findAll() {
        return ResponseEntity.ok(artistService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(artistService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid ArtistRequestDTO dto
    ) {
        return ResponseEntity.ok(artistService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        artistService.delete(id);

        return ResponseEntity.noContent().build();
    }
}