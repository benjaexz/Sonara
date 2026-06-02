package io.sonara.dto;

import java.util.UUID;

public record PlaylistTrackResponseDTO(

        UUID id,
        TrackResponseDTO track,
        Integer position

) {
}