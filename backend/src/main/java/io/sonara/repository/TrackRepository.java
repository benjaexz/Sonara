package io.sonara.repository;

import io.sonara.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TrackRepository extends JpaRepository<Track, UUID> {

    Optional<Track> findByExternalId(String externalId);
}