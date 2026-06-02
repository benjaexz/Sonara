package io.sonara.repository;

import io.sonara.entity.Playlist;
import io.sonara.entity.PlaylistTrack;
import io.sonara.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlaylistTrackRepository extends JpaRepository<PlaylistTrack, UUID> {

    List<PlaylistTrack> findByPlaylistOrderByPositionAsc(Playlist playlist);

    boolean existsByPlaylistAndTrack(Playlist playlist, Track track);
}