package io.sonara.repository;

import io.sonara.entity.Favorite;
import io.sonara.entity.Track;
import io.sonara.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    boolean existsByUserAndTrack(User user, Track track);

    List<Favorite> findByUser(User user);

    void deleteByUserAndTrack(User user, Track track);
}