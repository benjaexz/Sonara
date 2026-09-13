package io.sonara.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TrackCommentResponseDTO(

        UUID id,
        String username,
        String content,
        LocalDateTime createdAt

) {}