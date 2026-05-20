package io.sonara.dto;

import jakarta.validation.constraints.NotBlank;

public record ArtistRequestDTO(

        @NotBlank(message = "Artist name is required")
        String name

) {
}