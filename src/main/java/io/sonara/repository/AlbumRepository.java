package io.sonara.repository;

import io.sonara.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AlbumRepository extends JpaRepository<Album, UUID> {

    Optional<Album> findByTitle(String title);
}