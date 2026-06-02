package io.sonara.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record FavoriteResponseDTO(
        UUID id,
        TrackResponseDTO track,
        LocalDateTime createdAt
) {
}