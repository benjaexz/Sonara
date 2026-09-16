package io.sonara.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sonara.dto.PlaylistRequestDTO;
import io.sonara.dto.PlaylistResponseDTO;
import io.sonara.entity.User;
import io.sonara.exception.DuplicateResourceException;
import io.sonara.exception.GlobalExceptionHandler;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.UserRepository;
import io.sonara.security.JwtService;
import io.sonara.service.PlaylistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlaylistController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class PlaylistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PlaylistService playlistService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    private Authentication auth;
    private final String email = "jaco@sonara.io";

    @BeforeEach
    void setUp() {
        User mockUser = mock(User.class);
        when(mockUser.getEmail()).thenReturn(email);

        auth = new UsernamePasswordAuthenticationToken(mockUser, null, Collections.emptyList());
    }

    @Test
    @DisplayName("POST /playlists - Deve retornar 201 Created ao criar playlist com sucesso")
    void shouldReturn201WhenCreatingPlaylist() throws Exception {
        PlaylistRequestDTO request = new PlaylistRequestDTO("Minha Playlist");
        UUID playlistId = UUID.randomUUID();
        PlaylistResponseDTO response = new PlaylistResponseDTO(playlistId, request.name(), Collections.emptyList());

        when(playlistService.createPlaylist(eq(email), any(PlaylistRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/playlists")
                .principal(auth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(playlistId.toString()))
                .andExpect(jsonPath("$.name").value("Minha Playlist"));

        verify(playlistService, times(1)).createPlaylist(eq(email), any(PlaylistRequestDTO.class));
    }

    @Test
    @DisplayName("POST /playlists - Deve retornar 409 Conflict quando playlist de mesmo nome já existir")
    void shouldReturn409WhenPlaylistNameAlreadyExists() throws Exception {
        PlaylistRequestDTO request = new PlaylistRequestDTO("Rock");

        when(playlistService.createPlaylist(eq(email), any(PlaylistRequestDTO.class)))
                .thenThrow(new DuplicateResourceException("Playlist already exists"));

        mockMvc.perform(post("/playlists")
                .principal(auth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        verify(playlistService, times(1)).createPlaylist(eq(email), any(PlaylistRequestDTO.class));
    }

    @Test
    @DisplayName("GET /playlists - Deve retornar 200 OK com a lista de playlists do usuário")
    void shouldReturn200AndListPlaylists() throws Exception {
        UUID playlistId = UUID.randomUUID();
        PlaylistResponseDTO response = new PlaylistResponseDTO(playlistId, "Favoritas", Collections.emptyList());

        when(playlistService.listPlaylists(email)).thenReturn(List.of(response));

        mockMvc.perform(get("/playlists")
                .principal(auth)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Favoritas"));

        verify(playlistService, times(1)).listPlaylists(email);
    }

    @Test
    @DisplayName("POST /playlists/{playlistId}/tracks/{trackId} - Deve retornar 200 OK ao adicionar faixa")
    void shouldReturn200WhenAddingTrackToPlaylist() throws Exception {
        UUID playlistId = UUID.randomUUID();
        UUID trackId = UUID.randomUUID();
        PlaylistResponseDTO response = new PlaylistResponseDTO(playlistId, "Vibes", Collections.emptyList());

        when(playlistService.addTrackToPlaylist(playlistId, trackId, email)).thenReturn(response);

        mockMvc.perform(post("/playlists/{playlistId}/tracks/{trackId}", playlistId, trackId)
                .principal(auth)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(playlistId.toString()));

        verify(playlistService, times(1)).addTrackToPlaylist(playlistId, trackId, email);
    }

    @Test
    @DisplayName("POST /playlists/{playlistId}/tracks/{trackId} - Deve retornar 404 Not Found quando playlist não existir")
    void shouldReturn404WhenPlaylistNotFound() throws Exception {
        UUID playlistId = UUID.randomUUID();
        UUID trackId = UUID.randomUUID();

        when(playlistService.addTrackToPlaylist(playlistId, trackId, email))
                .thenThrow(new ResourceNotFoundException("Playlist not found"));

        mockMvc.perform(post("/playlists/{playlistId}/tracks/{trackId}", playlistId, trackId)
                .principal(auth)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(playlistService, times(1)).addTrackToPlaylist(playlistId, trackId, email);
    }

    @Test
    @DisplayName("DELETE /playlists/{playlistId} - Deve retornar 204 No Content ao deletar playlist")
    void shouldReturn204WhenDeletingPlaylist() throws Exception {
        UUID playlistId = UUID.randomUUID();

        doNothing().when(playlistService).deletePlaylist(playlistId, email);

        mockMvc.perform(delete("/playlists/{playlistId}", playlistId)
                .principal(auth))
                .andExpect(status().isNoContent());

        verify(playlistService, times(1)).deletePlaylist(playlistId, email);
    }
}