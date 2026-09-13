package io.sonara.controller;

import io.sonara.dto.TrackCommentRequestDTO;
import io.sonara.dto.TrackCommentResponseDTO;
import io.sonara.service.TrackCommentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class TrackCommentController {

    private final TrackCommentService trackCommentService;

    public TrackCommentController(TrackCommentService trackCommentService) {
        this.trackCommentService = trackCommentService;
    }

    @PostMapping("/{trackId}")
    public TrackCommentResponseDTO createComment(
            @PathVariable UUID trackId,
            @RequestBody @Valid TrackCommentRequestDTO dto,
            Authentication authentication
    ) {
        return trackCommentService.createComment(trackId, authentication.getName(), dto);
    }

    @GetMapping("/{trackId}")
    public List<TrackCommentResponseDTO> getCommentsByTrack(@PathVariable UUID trackId) {
        return trackCommentService.getCommentsByTrack(trackId);
    }

    @GetMapping("/my")
    public List<TrackCommentResponseDTO> getMyComments(Authentication authentication) {
        return trackCommentService.getMyComments(authentication.getName());
    }

    @PutMapping("/{commentId}")
    public TrackCommentResponseDTO updateComment(
            @PathVariable UUID commentId,
            @RequestBody @Valid TrackCommentRequestDTO dto,
            Authentication authentication
    ) {
        return trackCommentService.updateComment(commentId, authentication.getName(), dto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @PathVariable UUID commentId,
            Authentication authentication
    ) {
        trackCommentService.deleteComment(commentId, authentication.getName());
    }
}