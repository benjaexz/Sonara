package io.sonara.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Playlist() {
    }

    public Playlist(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    public void setName(String name) {
        this.name = name;
    }
}