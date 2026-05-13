package io.sonara.dto;

import jakarta.validation.constraints.NotBlank;

public class GenreRequestDTO {

    @NotBlank(message = "Genre name is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}