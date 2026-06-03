package io.sonara.service;

import io.sonara.dto.TrackCommentRequestDTO;
import io.sonara.dto.TrackCommentResponseDTO;
import io.sonara.entity.Track;
import io.sonara.entity.TrackComment;
import io.sonara.entity.User;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.TrackCommentRepository;
import io.sonara.repository.TrackRepository;
import io.sonara.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TrackCommentService {

    private final TrackCommentRepository trackCommentRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;

    public TrackCommentService(
            TrackCommentRepository trackCommentRepository,
            TrackRepository trackRepository,
            UserRepository userRepository
    ) {
        this.trackCommentRepository = trackCommentRepository;
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
    }

    public TrackCommentResponseDTO createComment(UUID trackId, String email, TrackCommentRequestDTO dto) {
        User user = findUserByEmail(email);
        Track track = findTrackById(trackId);

        TrackComment comment = new TrackComment(user, track, dto.content());

        TrackComment savedComment = trackCommentRepository.save(comment);

        return toResponseDTO(savedComment);
    }

    public List<TrackCommentResponseDTO> getCommentsByTrack(UUID trackId) {
        Track track = findTrackById(trackId);

        return trackCommentRepository.findByTrack(track)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<TrackCommentResponseDTO> getMyComments(String email) {
        User user = findUserByEmail(email);

        return trackCommentRepository.findByUser(user)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public TrackCommentResponseDTO updateComment(UUID commentId, String email, TrackCommentRequestDTO dto) {
        User user = findUserByEmail(email);

        TrackComment comment = trackCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Comment not found");
        }

        comment.setContent(dto.content());

        TrackComment updatedComment = trackCommentRepository.save(comment);

        return toResponseDTO(updatedComment);
    }

    public void deleteComment(UUID commentId, String email) {
        User user = findUserByEmail(email);

        TrackComment comment = trackCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Comment not found");
        }

        trackCommentRepository.delete(comment);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Track findTrackById(UUID trackId) {
        return trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));
    }

    private TrackCommentResponseDTO toResponseDTO(TrackComment comment) {
        return new TrackCommentResponseDTO(
                comment.getId(),
                comment.getUser().getUsername(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}