package io.sonara.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class TrackRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private Integer durationSeconds;

    @NotNull(message = "Artist ID is required")
    private UUID artistId;

    private UUID albumId;

    private UUID genreId;

    public TrackRequestDTO() {
    }

    public String getTitle() {
        return title;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public UUID getArtistId() {
        return artistId;
    }

    public UUID getAlbumId() {
        return albumId;
    }

    public UUID getGenreId() {
        return genreId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setArtistId(UUID artistId) {
        this.artistId = artistId;
    }

    public void setAlbumId(UUID albumId) {
        this.albumId = albumId;
    }

    public void setGenreId(UUID genreId) {
        this.genreId = genreId;
    }
}