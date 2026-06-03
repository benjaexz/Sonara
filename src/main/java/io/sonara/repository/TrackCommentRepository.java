package io.sonara.repository;

import io.sonara.entity.Track;
import io.sonara.entity.TrackComment;
import io.sonara.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrackCommentRepository extends JpaRepository<TrackComment, UUID> {

    List<TrackComment> findByTrack(Track track);

    List<TrackComment> findByUser(User user);
}