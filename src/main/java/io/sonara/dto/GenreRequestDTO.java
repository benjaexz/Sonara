package io.sonara.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GenreRequestDTO {

    @NotBlank(message = "Genre name is required")
    @Size(max = 50, message = "Genre name must be at most 50 characters")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}