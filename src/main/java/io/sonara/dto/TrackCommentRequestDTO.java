package io.sonara.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TrackCommentRequestDTO(

        @NotBlank
        @Size(max = 1000)
        String content

) {}