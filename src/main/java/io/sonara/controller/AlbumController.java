package io.sonara.controller;

import io.sonara.dto.AlbumRequestDTO;
import io.sonara.dto.AlbumResponseDTO;
import io.sonara.service.AlbumService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @PostMapping
    public ResponseEntity<AlbumResponseDTO> save(@RequestBody @Valid AlbumRequestDTO dto) {
        AlbumResponseDTO createdAlbum = albumService.save(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAlbum);
    }

    @GetMapping
    public ResponseEntity<List<AlbumResponseDTO>> findAll() {
        return ResponseEntity.ok(albumService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(albumService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid AlbumRequestDTO dto
    ) {
        return ResponseEntity.ok(albumService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        albumService.delete(id);

        return ResponseEntity.noContent().build();
    }
}