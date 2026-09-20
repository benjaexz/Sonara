package io.sonara.repository;

import io.sonara.entity.Album;
import io.sonara.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AlbumRepository extends JpaRepository<Album, UUID> {

    Optional<Album> findByTitle(String title);

    Optional<Album> findFirstByTitleAndArtist(String title, Artist artist);
}