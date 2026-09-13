package io.sonara.repository;

import io.sonara.entity.Playlist;
import io.sonara.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {

    List<Playlist> findByUser(User user);

    boolean existsByUserAndName(User user, String name);

    void deleteByIdAndUser(UUID id, User user);
}