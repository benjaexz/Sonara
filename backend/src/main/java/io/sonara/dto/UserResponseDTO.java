package io.sonara.dto;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String email
) {}