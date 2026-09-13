package io.sonara.service;

import io.sonara.dto.FavoriteResponseDTO;
import io.sonara.entity.Favorite;
import io.sonara.entity.Track;
import io.sonara.entity.User;
import io.sonara.exception.DuplicateResourceException;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.FavoriteRepository;
import io.sonara.repository.TrackRepository;
import io.sonara.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    private final TrackService trackService;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            TrackRepository trackRepository,
            UserRepository userRepository,
            TrackService trackService
    ) {
        this.favoriteRepository = favoriteRepository;
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
        this.trackService = trackService;
    }

    public FavoriteResponseDTO addFavorite(String email, UUID trackId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        if (favoriteRepository.existsByUserAndTrack(user, track)) {
            throw new DuplicateResourceException("Track already favorited");
        }

        Favorite favorite = new Favorite(user, track);
        Favorite savedFavorite = favoriteRepository.save(favorite);

        return toResponseDTO(savedFavorite);
    }

    public List<FavoriteResponseDTO> listFavorites(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return favoriteRepository.findByUser(user)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public void removeFavorite(UUID trackId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        if (!favoriteRepository.existsByUserAndTrack(user, track)) {
            throw new ResourceNotFoundException("Favorite not found");
        }

        favoriteRepository.deleteByUserAndTrack(user, track);
    }

    private FavoriteResponseDTO toResponseDTO(Favorite favorite) {
        return new FavoriteResponseDTO(
                favorite.getId(),
                trackService.toResponseDTO(favorite.getTrack()),
                favorite.getCreatedAt()
        );
    }
}