package io.sonara.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlaylistRequestDTO(

        @NotBlank(message = "Playlist name is required")
        @Size(min = 2, max = 80, message = "Playlist name must have between 2 and 80 characters")
        String name

) {
}