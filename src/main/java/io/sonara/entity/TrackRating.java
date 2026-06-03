package io.sonara.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "track_ratings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "track_id"})
        }
)
public class TrackRating {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    @Column(nullable = false)
    private Integer rating;

    public TrackRating() {
    }

    public TrackRating(User user, Track track, Integer rating) {
        this.user = user;
        this.track = track;
        this.rating = rating;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}