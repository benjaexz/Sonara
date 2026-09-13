package io.sonara.dto;

import java.util.UUID;

public record TrackRatingResponseDTO(

        UUID id,
        UUID trackId,
        String trackTitle,
        Integer rating

) {}