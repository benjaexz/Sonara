package io.sonara.dto;

import java.util.UUID;

public class GenreResponseDTO {

    private UUID id;
    private String name;

    public GenreResponseDTO(UUID id, String name) {
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
