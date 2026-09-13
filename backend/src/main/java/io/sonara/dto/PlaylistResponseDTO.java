package io.sonara.dto;

import java.util.List;
import java.util.UUID;

public record PlaylistResponseDTO(

        UUID id,
        String name,
        List<PlaylistTrackResponseDTO> tracks

) {
}