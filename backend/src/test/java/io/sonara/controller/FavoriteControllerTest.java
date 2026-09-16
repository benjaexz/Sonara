package io.sonara.controller;

import io.sonara.dto.FavoriteResponseDTO;
import io.sonara.dto.TrackResponseDTO;
import io.sonara.entity.User;
import io.sonara.exception.DuplicateResourceException;
import io.sonara.exception.GlobalExceptionHandler;
import io.sonara.exception.ResourceNotFoundException;
import io.sonara.repository.UserRepository;
import io.sonara.security.JwtService;
import io.sonara.service.FavoriteService;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriteController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FavoriteService favoriteService;

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
    @DisplayName("POST /favorites/{trackId} - Deve retornar 201 Created ao favoritar faixa")
    void shouldReturn201WhenAddingFavorite() throws Exception {
        UUID trackId = UUID.randomUUID();
        UUID favoriteId = UUID.randomUUID();
        TrackResponseDTO trackDTO = new TrackResponseDTO(trackId, "Change", 300, null, null, null);
        FavoriteResponseDTO responseDTO = new FavoriteResponseDTO(favoriteId, trackDTO, LocalDateTime.now());

        when(favoriteService.addFavorite(email, trackId)).thenReturn(responseDTO);

        mockMvc.perform(post("/favorites/{trackId}", trackId)
                .principal(auth)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(favoriteId.toString()))
                .andExpect(jsonPath("$.track.title").value("Change"));

        verify(favoriteService, times(1)).addFavorite(email, trackId);
    }

    @Test
    @DisplayName("POST /favorites/{trackId} - Deve retornar 409 Conflict ao tentar favoritar duplicado")
    void shouldReturn409WhenDuplicateFavorite() throws Exception {
        UUID trackId = UUID.randomUUID();

        when(favoriteService.addFavorite(email, trackId))
                .thenThrow(new DuplicateResourceException("Track already favorited"));

        mockMvc.perform(post("/favorites/{trackId}", trackId)
                .principal(auth)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        verify(favoriteService, times(1)).addFavorite(email, trackId);
    }

    @Test
    @DisplayName("POST /favorites/{trackId} - Deve retornar 404 Not Found quando faixa não existir")
    void shouldReturn404WhenTrackNotFound() throws Exception {
        UUID trackId = UUID.randomUUID();

        when(favoriteService.addFavorite(email, trackId))
                .thenThrow(new ResourceNotFoundException("Track not found"));

        mockMvc.perform(post("/favorites/{trackId}", trackId)
                .principal(auth)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(favoriteService, times(1)).addFavorite(email, trackId);
    }

    @Test
    @DisplayName("GET /favorites - Deve retornar 200 OK com lista de favoritos")
    void shouldReturn200AndListFavorites() throws Exception {
        UUID trackId = UUID.randomUUID();
        UUID favoriteId = UUID.randomUUID();
        TrackResponseDTO trackDTO = new TrackResponseDTO(trackId, "Digital Bath", 250, null, null, null);
        FavoriteResponseDTO responseDTO = new FavoriteResponseDTO(favoriteId, trackDTO, LocalDateTime.now());

        when(favoriteService.listFavorites(email)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/favorites")
                .principal(auth)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(favoriteId.toString()))
                .andExpect(jsonPath("$[0].track.title").value("Digital Bath"));

        verify(favoriteService, times(1)).listFavorites(email);
    }

    @Test
    @DisplayName("DELETE /favorites/{trackId} - Deve retornar 204 No Content ao remover")
    void shouldReturn204WhenRemovingFavorite() throws Exception {
        UUID trackId = UUID.randomUUID();

        doNothing().when(favoriteService).removeFavorite(trackId, email);

        mockMvc.perform(delete("/favorites/{trackId}", trackId)
                .principal(auth))
                .andExpect(status().isNoContent());

        verify(favoriteService, times(1)).removeFavorite(trackId, email);
    }
}