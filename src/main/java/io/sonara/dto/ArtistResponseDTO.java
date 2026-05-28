package io.sonara.dto;

import java.util.UUID;

public class ArtistResponseDTO {

    private UUID id;
    private String name;

    public ArtistResponseDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}