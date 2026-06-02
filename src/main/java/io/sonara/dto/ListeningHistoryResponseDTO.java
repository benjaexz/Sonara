package io.sonara.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ListeningHistoryResponseDTO(
        UUID id,
        TrackResponseDTO track,
        LocalDateTime listenedAt
) {
}