package io.sonara.dto;

import java.util.UUID;

public class AlbumResponseDTO {

    private UUID id;
    private String title;
    private Integer releaseYear;
    private ArtistResponseDTO artist;

    public AlbumResponseDTO(UUID id, String title, Integer releaseYear, ArtistResponseDTO artist) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.artist = artist;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public ArtistResponseDTO getArtist() {
        return artist;
    }
}