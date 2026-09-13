package io.sonara.dto;

import java.util.UUID;

public class TrackResponseDTO {

    private UUID id;
    private String title;
    private Integer durationSeconds;
    private ArtistResponseDTO artist;
    private AlbumResponseDTO album;
    private GenreResponseDTO genre;

    public TrackResponseDTO(
            UUID id,
            String title,
            Integer durationSeconds,
            ArtistResponseDTO artist,
            AlbumResponseDTO album,
            GenreResponseDTO genre
    ) {
        this.id = id;
        this.title = title;
        this.durationSeconds = durationSeconds;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public ArtistResponseDTO getArtist() {
        return artist;
    }

    public AlbumResponseDTO getAlbum() {
        return album;
    }

    public GenreResponseDTO getGenre() {
        return genre;
    }
}