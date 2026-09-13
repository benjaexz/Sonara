package io.sonara.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AlbumRequestDTO(

        @NotBlank(message = "Album title is required")
        String title,

        @NotNull(message = "Release year is required")
        Integer releaseYear,

        @NotNull(message = "Artist id is required")
        UUID artistId

) {
}