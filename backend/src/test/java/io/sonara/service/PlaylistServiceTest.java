package io.sonara.service;

import io.sonara.dto.PlaylistRequestDTO;
import io.sonara.dto.PlaylistResponseDTO;
import io.sonara.entity.Playlist;
import io.sonara.entity.PlaylistTrack;
import io.sonara.entity.Track;
import io.sonara.entity.User;
import io.sonara.exception.DuplicateResourceException;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.PlaylistRepository;
import io.sonara.repository.PlaylistTrackRepository;
import io.sonara.repository.TrackRepository;
import io.sonara.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PlaylistServiceTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private PlaylistTrackRepository playlistTrackRepository;

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrackService trackService;

    @InjectMocks
    private PlaylistService playlistService;

    @Test
    @DisplayName("Deve criar playlist com sucesso quando dados forem válidos")
    void shouldCreatePlaylistSuccessfully() {
        String email = "jaco@sonara.io";
        UUID playlistId = UUID.randomUUID();
        PlaylistRequestDTO request = new PlaylistRequestDTO("Minha Playlist");

        User user = mock(User.class);
        when(user.getEmail()).thenReturn(email);

        Playlist savedPlaylist = mock(Playlist.class);
        when(savedPlaylist.getId()).thenReturn(playlistId);
        when(savedPlaylist.getName()).thenReturn(request.name());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(playlistRepository.existsByUserAndName(user, request.name())).thenReturn(false);
        when(playlistRepository.save(any(Playlist.class))).thenReturn(savedPlaylist);
        when(playlistTrackRepository.findByPlaylistOrderByPositionAsc(savedPlaylist))
                .thenReturn(Collections.emptyList());

        PlaylistResponseDTO result = playlistService.createPlaylist(email, request);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(playlistId);
        assertThat(result.name()).isEqualTo("Minha Playlist");
        verify(playlistRepository, times(1)).save(any(Playlist.class));
    }

    @Test
    @DisplayName("Deve lançar DuplicateResourceException quando playlist de mesmo nome já existir para o usuário")
    void shouldThrowDuplicateResourceExceptionWhenPlaylistNameAlreadyExists() {
        String email = "jaco@sonara.io";
        PlaylistRequestDTO request = new PlaylistRequestDTO("Rock Clássico");

        User user = mock(User.class);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(playlistRepository.existsByUserAndName(user, request.name())).thenReturn(true);

        assertThatThrownBy(() -> playlistService.createPlaylist(email, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Playlist already exists");

        verify(playlistRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve adicionar faixa na playlist com posição incremental calculada")
    void shouldAddTrackToPlaylistSuccessfully() {
        String email = "jaco@sonara.io";
        UUID userId = UUID.randomUUID();
        UUID playlistId = UUID.randomUUID();
        UUID trackId = UUID.randomUUID();

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Playlist playlist = mock(Playlist.class);
        when(playlist.getUser()).thenReturn(user);
        when(playlist.getId()).thenReturn(playlistId);
        when(playlist.getName()).thenReturn("Metal Vibes");

        Track track = new Track();
        track.setId(trackId);

        PlaylistTrack existingTrack = mock(PlaylistTrack.class);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));
        when(playlistTrackRepository.existsByPlaylistAndTrack(playlist, track)).thenReturn(false);
        when(playlistTrackRepository.findByPlaylistOrderByPositionAsc(playlist)).thenReturn(List.of(existingTrack));

        PlaylistResponseDTO result = playlistService.addTrackToPlaylist(playlistId, trackId, email);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(playlistId);
        verify(playlistTrackRepository, times(1)).save(any(PlaylistTrack.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando usuário tentar adicionar faixa em playlist de outro proprietário")
    void shouldThrowExceptionWhenUserDoesNotOwnPlaylist() {
        String email = "hacker@sonara.io";
        UUID ownerId = UUID.randomUUID();
        UUID callerId = UUID.randomUUID();
        UUID playlistId = UUID.randomUUID();
        UUID trackId = UUID.randomUUID();

        User owner = mock(User.class);
        when(owner.getId()).thenReturn(ownerId);

        User caller = mock(User.class);
        when(caller.getId()).thenReturn(callerId);

        Playlist playlist = mock(Playlist.class);
        when(playlist.getUser()).thenReturn(owner);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(caller));
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));

        assertThatThrownBy(() -> playlistService.addTrackToPlaylist(playlistId, trackId, email))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Playlist not found");

        verify(playlistTrackRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar DuplicateResourceException quando tentar adicionar faixa já existente na playlist")
    void shouldThrowExceptionWhenTrackAlreadyExistsInPlaylist() {
        String email = "jaco@sonara.io";
        UUID userId = UUID.randomUUID();
        UUID playlistId = UUID.randomUUID();
        UUID trackId = UUID.randomUUID();

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Playlist playlist = mock(Playlist.class);
        when(playlist.getUser()).thenReturn(user);

        Track track = new Track();
        track.setId(trackId);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));
        when(playlistTrackRepository.existsByPlaylistAndTrack(playlist, track)).thenReturn(true);

        assertThatThrownBy(() -> playlistService.addTrackToPlaylist(playlistId, trackId, email))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Track already exists in playlist");

        verify(playlistTrackRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve remover faixa da playlist com sucesso")
    void shouldRemoveTrackFromPlaylistSuccessfully() {
        String email = "jaco@sonara.io";
        UUID userId = UUID.randomUUID();
        UUID playlistId = UUID.randomUUID();
        UUID trackId = UUID.randomUUID();

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Playlist playlist = mock(Playlist.class);
        when(playlist.getUser()).thenReturn(user);
        when(playlist.getId()).thenReturn(playlistId);

        Track track = new Track();
        track.setId(trackId);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));
        when(playlistTrackRepository.existsByPlaylistAndTrack(playlist, track)).thenReturn(true);
        when(playlistTrackRepository.findByPlaylistOrderByPositionAsc(playlist)).thenReturn(Collections.emptyList());

        PlaylistResponseDTO result = playlistService.removeTrackFromPlaylist(playlistId, trackId, email);

        assertThat(result).isNotNull();
        verify(playlistTrackRepository, times(1)).deleteByPlaylistAndTrack(playlist, track);
    }

    @Test
    @DisplayName("Deve deletar playlist com sucesso quando for o proprietário")
    void shouldDeletePlaylistSuccessfully() {
        String email = "jaco@sonara.io";
        UUID userId = UUID.randomUUID();
        UUID playlistId = UUID.randomUUID();

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Playlist playlist = mock(Playlist.class);
        when(playlist.getUser()).thenReturn(user);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));

        playlistService.deletePlaylist(playlistId, email);

        verify(playlistRepository, times(1)).deleteByIdAndUser(playlistId, user);
    }
}