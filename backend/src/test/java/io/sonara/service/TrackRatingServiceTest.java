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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrackRatingServiceTest {

    @Mock
    private TrackRatingRepository trackRatingRepository;

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private TrackRatingService trackRatingService;

    private final String userEmail = "jaco@sonara.io";
    private User authenticatedUser;

    @BeforeEach
    void setUp() {
        authenticatedUser = mock(User.class);
        when(authenticatedUser.getEmail()).thenReturn(userEmail);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(authenticatedUser));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve avaliar uma faixa com sucesso quando dados forem válidos")
    void shouldRateTrackSuccessfully() {
        UUID trackId = UUID.randomUUID();
        UUID ratingId = UUID.randomUUID();
        TrackRatingRequestDTO request = new TrackRatingRequestDTO(5);

        Track track = mock(Track.class);
        when(track.getId()).thenReturn(trackId);
        when(track.getTitle()).thenReturn("Passenger");

        TrackRating savedRating = mock(TrackRating.class);
        when(savedRating.getId()).thenReturn(ratingId);
        when(savedRating.getTrack()).thenReturn(track);
        when(savedRating.getRating()).thenReturn(5);

        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));
        when(trackRatingRepository.existsByUserAndTrack(authenticatedUser, track)).thenReturn(false);
        when(trackRatingRepository.save(any(TrackRating.class))).thenReturn(savedRating);

        TrackRatingResponseDTO result = trackRatingService.rateTrack(trackId, request);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(ratingId);
        assertThat(result.rating()).isEqualTo(5);
        assertThat(result.trackTitle()).isEqualTo("Passenger");
        verify(trackRatingRepository, times(1)).save(any(TrackRating.class));
    }

    @Test
    @DisplayName("Deve lançar DuplicateResourceException ao tentar avaliar uma faixa já avaliada pelo usuário")
    void shouldThrowDuplicateResourceExceptionWhenTrackAlreadyRated() {
        UUID trackId = UUID.randomUUID();
        TrackRatingRequestDTO request = new TrackRatingRequestDTO(4);

        Track track = mock(Track.class);

        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));
        when(trackRatingRepository.existsByUserAndTrack(authenticatedUser, track)).thenReturn(true);

        assertThatThrownBy(() -> trackRatingService.rateTrack(trackId, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Track already rated");

        verify(trackRatingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar a avaliação de uma faixa existente")
    void shouldUpdateRatingSuccessfully() {
        UUID trackId = UUID.randomUUID();
        UUID ratingId = UUID.randomUUID();
        TrackRatingRequestDTO request = new TrackRatingRequestDTO(3);

        Track track = mock(Track.class);
        when(track.getId()).thenReturn(trackId);
        when(track.getTitle()).thenReturn("Be Quiet and Drive");

        TrackRating existingRating = new TrackRating(authenticatedUser, track, 5);

        TrackRating updatedRating = mock(TrackRating.class);
        when(updatedRating.getId()).thenReturn(ratingId);
        when(updatedRating.getTrack()).thenReturn(track);
        when(updatedRating.getRating()).thenReturn(3);

        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));
        when(trackRatingRepository.findByUserAndTrack(authenticatedUser, track))
                .thenReturn(Optional.of(existingRating));
        when(trackRatingRepository.save(existingRating)).thenReturn(updatedRating);

        TrackRatingResponseDTO result = trackRatingService.updateRating(trackId, request);

        assertThat(result).isNotNull();
        assertThat(result.rating()).isEqualTo(3);
        verify(trackRatingRepository, times(1)).save(existingRating);
    }

    @Test
    @DisplayName("Deve listar todas as avaliações do usuário autenticado")
    void shouldListUserRatings() {
        UUID trackId = UUID.randomUUID();
        Track track = mock(Track.class);
        when(track.getId()).thenReturn(trackId);
        when(track.getTitle()).thenReturn("Digital Bath");

        TrackRating rating = mock(TrackRating.class);
        when(rating.getId()).thenReturn(UUID.randomUUID());
        when(rating.getTrack()).thenReturn(track);
        when(rating.getRating()).thenReturn(5);

        when(trackRatingRepository.findByUser(authenticatedUser)).thenReturn(List.of(rating));

        List<TrackRatingResponseDTO> result = trackRatingService.getMyRatings();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).rating()).isEqualTo(5);
        assertThat(result.get(0).trackTitle()).isEqualTo("Digital Bath");
        verify(trackRatingRepository, times(1)).findByUser(authenticatedUser);
    }

    @Test
    @DisplayName("Deve remover avaliação com sucesso")
    void shouldDeleteRatingSuccessfully() {
        UUID trackId = UUID.randomUUID();
        Track track = mock(Track.class);

        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));
        when(trackRatingRepository.existsByUserAndTrack(authenticatedUser, track)).thenReturn(true);

        trackRatingService.deleteRating(trackId);

        verify(trackRatingRepository, times(1)).deleteByUserAndTrack(authenticatedUser, track);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar deletar avaliação inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentRating() {
        UUID trackId = UUID.randomUUID();
        Track track = mock(Track.class);

        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));
        when(trackRatingRepository.existsByUserAndTrack(authenticatedUser, track)).thenReturn(false);

        assertThatThrownBy(() -> trackRatingService.deleteRating(trackId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Track rating not found");

        verify(trackRatingRepository, never()).deleteByUserAndTrack(any(), any());
    }
}