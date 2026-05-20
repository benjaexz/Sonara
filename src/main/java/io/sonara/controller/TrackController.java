package io.sonara.controller;

import io.sonara.dto.TrackRequestDTO;
import io.sonara.entity.Track;
import io.sonara.service.TrackService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @PostMapping
    public ResponseEntity<Track> createTrack(@Valid @RequestBody TrackRequestDTO dto) {

        Track createdTrack = trackService.createTrack(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTrack);
    }

    @GetMapping 
    public ResponseEntity<List<Track>> getAllTracks() {

        return ResponseEntity.ok(trackService.getAllTracks());
    }
}