package io.sonara.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "listening_history")
public class ListeningHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    private LocalDateTime listenedAt;

    public ListeningHistory() {
    }

    public ListeningHistory(User user, Track track) {
        this.user = user;
        this.track = track;
        this.listenedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Track getTrack() {
        return track;
    }

    public LocalDateTime getListenedAt() {
        return listenedAt;
    }
}