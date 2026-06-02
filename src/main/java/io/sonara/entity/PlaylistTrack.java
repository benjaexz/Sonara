package io.sonara.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "playlist_tracks")
public class PlaylistTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    private Integer position;

    public PlaylistTrack() {
    }

    public PlaylistTrack(Playlist playlist, Track track, Integer position) {
        this.playlist = playlist;
        this.track = track;
        this.position = position;
    }

    public UUID getId() {
        return id;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public Track getTrack() {
        return track;
    }

    public Integer getPosition() {
        return position;
    }
}