package io.sonara.controller;

import io.sonara.dto.TrackRatingRequestDTO;
import io.sonara.dto.TrackRatingResponseDTO;
import io.sonara.service.TrackRatingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/ratings")
public class TrackRatingController {

    private final TrackRatingService trackRatingService;

    public TrackRatingController(TrackRatingService trackRatingService) {
        this.trackRatingService = trackRatingService;
    }

    @GetMapping("/my")
    public List<TrackRatingResponseDTO> getMyRatings() {
        return trackRatingService.getMyRatings();
    }

    @PostMapping("/{trackId}")
    @ResponseStatus(HttpStatus.CREATED)
    public TrackRatingResponseDTO rateTrack(
            @PathVariable UUID trackId,
            @RequestBody @Valid TrackRatingRequestDTO request
    ) {
        return trackRatingService.rateTrack(trackId, request);
    }

    @PutMapping("/{trackId}")
    public TrackRatingResponseDTO updateRating(
            @PathVariable UUID trackId,
            @RequestBody @Valid TrackRatingRequestDTO request
    ) {
        return trackRatingService.updateRating(trackId, request);
    }

    @DeleteMapping("/{trackId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRating(@PathVariable UUID trackId) {
        trackRatingService.deleteRating(trackId);
    }
}