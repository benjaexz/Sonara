package io.sonara.repository;

import io.sonara.entity.Track;
import io.sonara.entity.TrackRating;
import io.sonara.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrackRatingRepository extends JpaRepository<TrackRating, UUID> {

    boolean existsByUserAndTrack(User user, Track track);

    Optional<TrackRating> findByUserAndTrack(User user, Track track);

    List<TrackRating> findByUser(User user);

    void deleteByUserAndTrack(User user, Track track);
}
