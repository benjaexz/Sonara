package io.sonara.service;

import io.sonara.dto.TrackRatingRequestDTO;
import io.sonara.dto.TrackRatingResponseDTO;
import io.sonara.entity.Track;
import io.sonara.entity.TrackRating;
import io.sonara.entity.User;
import io.sonara.exception.DuplicateResourceException;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.TrackRatingRepository;
import io.sonara.repository.TrackRepository;
import io.sonara.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;

@Service
public class TrackRatingService {

    private final TrackRatingRepository trackRatingRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;

    public TrackRatingService(
            TrackRatingRepository trackRatingRepository,
            TrackRepository trackRepository,
            UserRepository userRepository
    ) {
        this.trackRatingRepository = trackRatingRepository;
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
    }

    public TrackRatingResponseDTO updateRating(UUID trackId, TrackRatingRequestDTO request) {

        User user = getAuthenticatedUser();

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        TrackRating trackRating = trackRatingRepository.findByUserAndTrack(user, track)
                .orElseThrow(() -> new ResourceNotFoundException("Track rating not found"));

        trackRating.setRating(request.rating());

        TrackRating updatedRating = trackRatingRepository.save(trackRating);

        return toResponseDTO(updatedRating);
    }

    public List<TrackRatingResponseDTO> getMyRatings() {

        User user = getAuthenticatedUser();

        return trackRatingRepository.findByUser(user)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public TrackRatingResponseDTO rateTrack(UUID trackId, TrackRatingRequestDTO request) {
        User user = getAuthenticatedUser();

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        if (trackRatingRepository.existsByUserAndTrack(user, track)) {
            throw new DuplicateResourceException("Track already rated");
        }

        TrackRating trackRating = new TrackRating(user, track, request.rating());

        TrackRating savedRating = trackRatingRepository.save(trackRating);

        return toResponseDTO(savedRating);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private TrackRatingResponseDTO toResponseDTO(TrackRating trackRating) {
        return new TrackRatingResponseDTO(
                trackRating.getId(),
                trackRating.getTrack().getId(),
                trackRating.getTrack().getTitle(),
                trackRating.getRating()
        );
    }

    @Transactional
    public void deleteRating(UUID trackId) {

        User user = getAuthenticatedUser();

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        if (!trackRatingRepository.existsByUserAndTrack(user, track)) {
            throw new ResourceNotFoundException("Track rating not found");
        }

        trackRatingRepository.deleteByUserAndTrack(user, track);
    }
}