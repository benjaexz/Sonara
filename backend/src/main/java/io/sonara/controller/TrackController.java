package io.sonara.controller;

import io.sonara.dto.TrackRequestDTO;
import io.sonara.dto.TrackResponseDTO;
import io.sonara.service.TrackService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @PostMapping
    public ResponseEntity<TrackResponseDTO> createTrack(@Valid @RequestBody TrackRequestDTO dto) {

        TrackResponseDTO createdTrack = trackService.createTrack(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTrack);
    }

    @GetMapping
    public ResponseEntity<List<TrackResponseDTO>> getAllTracks() {

        return ResponseEntity.ok(trackService.getAllTracks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackResponseDTO> getTrackById(@PathVariable UUID id) {

        TrackResponseDTO track = trackService.getTrackById(id);

        return ResponseEntity.ok(track);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackResponseDTO> updateTrack(
            @PathVariable UUID id,
            @Valid @RequestBody TrackRequestDTO dto
    ) {
        TrackResponseDTO updatedTrack = trackService.updateTrack(id, dto);

        return ResponseEntity.ok(updatedTrack);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable UUID id) {

        trackService.deleteTrack(id);

        return ResponseEntity.noContent().build();
    }
}