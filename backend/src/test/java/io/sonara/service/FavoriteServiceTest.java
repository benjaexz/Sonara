package io.sonara.service;

import io.sonara.dto.FavoriteResponseDTO;
import io.sonara.dto.TrackResponseDTO;
import io.sonara.entity.Favorite;
import io.sonara.entity.Track;
import io.sonara.entity.User;
import io.sonara.exception.DuplicateResourceException;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.FavoriteRepository;
import io.sonara.repository.TrackRepository;
import io.sonara.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrackService trackService;

    @InjectMocks
    private FavoriteService favoriteService;

    @Test
    @DisplayName("Deve favoritar uma faixa com sucesso quando dados forem válidos")
    void shouldAddFavoriteSuccessfully() {
        String email = "jaco@sonara.io";
        UUID trackId = UUID.randomUUID();
        UUID favoriteId = UUID.randomUUID();

        User user = new User();
        user.setEmail(email);

        Track track = new Track();
        track.setId(trackId);

        Favorite savedFavorite = mock(Favorite.class);
        when(savedFavorite.getId()).thenReturn(favoriteId);
        when(savedFavorite.getTrack()).thenReturn(track);
        when(savedFavorite.getCreatedAt()).thenReturn(LocalDateTime.now());

        TrackResponseDTO trackDTO = new TrackResponseDTO(trackId, "Change", 300, null, null, null);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));
        when(favoriteRepository.existsByUserAndTrack(user, track)).thenReturn(false);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(savedFavorite);
        when(trackService.toResponseDTO(track)).thenReturn(trackDTO);

        FavoriteResponseDTO result = favoriteService.addFavorite(email, trackId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(favoriteId);
        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    @Test
    @DisplayName("Deve lançar DuplicateResourceException ao tentar favoritar faixa já favoritada")
    void shouldThrowDuplicateResourceExceptionWhenAlreadyFavorited() {
        String email = "jaco@sonara.io";
        UUID trackId = UUID.randomUUID();

        User user = new User();
        user.setEmail(email);

        Track track = new Track();
        track.setId(trackId);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));
        when(favoriteRepository.existsByUserAndTrack(user, track)).thenReturn(true);

        assertThatThrownBy(() -> favoriteService.addFavorite(email, trackId))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Track already favorited");

        verify(favoriteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o usuário não existir")
    void shouldThrowResourceNotFoundExceptionWhenUserNotFound() {
        String email = "inexistente@sonara.io";
        UUID trackId = UUID.randomUUID();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> favoriteService.addFavorite(email, trackId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(trackRepository, never()).findById(any());
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar os favoritos do usuário autenticado")
    void shouldListFavoritesForUser() {
        String email = "jaco@sonara.io";
        UUID favoriteId = UUID.randomUUID();

        User user = new User();
        user.setEmail(email);

        Track track = new Track();
        Favorite favorite = mock(Favorite.class);
        when(favorite.getId()).thenReturn(favoriteId);
        when(favorite.getTrack()).thenReturn(track);
        when(favorite.getCreatedAt()).thenReturn(LocalDateTime.now());

        TrackResponseDTO trackDTO = new TrackResponseDTO(UUID.randomUUID(), "Digital Bath", 250, null, null, null);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(favoriteRepository.findByUser(user)).thenReturn(List.of(favorite));
        when(trackService.toResponseDTO(track)).thenReturn(trackDTO);

        List<FavoriteResponseDTO> result = favoriteService.listFavorites(email);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(favoriteId);
        verify(favoriteRepository, times(1)).findByUser(user);
    }

    @Test
    @DisplayName("Deve remover favorito com sucesso quando existir")
    void shouldRemoveFavoriteSuccessfully() {
        String email = "jaco@sonara.io";
        UUID trackId = UUID.randomUUID();

        User user = new User();
        user.setEmail(email);

        Track track = new Track();
        track.setId(trackId);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));
        when(favoriteRepository.existsByUserAndTrack(user, track)).thenReturn(true);

        favoriteService.removeFavorite(trackId, email);

        verify(favoriteRepository, times(1)).deleteByUserAndTrack(user, track);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar remover favorito inexistente")
    void shouldThrowExceptionWhenRemovingNonExistentFavorite() {
        String email = "jaco@sonara.io";
        UUID trackId = UUID.randomUUID();

        User user = new User();
        user.setEmail(email);

        Track track = new Track();
        track.setId(trackId);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));
        when(favoriteRepository.existsByUserAndTrack(user, track)).thenReturn(false);

        assertThatThrownBy(() -> favoriteService.removeFavorite(trackId, email))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Favorite not found");

        verify(favoriteRepository, never()).deleteByUserAndTrack(any(), any());
    }
}