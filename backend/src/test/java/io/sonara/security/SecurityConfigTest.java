package io.sonara.security;

import io.sonara.controller.FavoriteController;
import io.sonara.repository.UserRepository;
import io.sonara.service.FavoriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteController.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FavoriteService favoriteService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @DisplayName("GET /favorites - Deve bloquear com 401 ou 403 quando não autenticado")
    void shouldRejectUnauthenticatedAccessToFavorites() throws Exception {
        mockMvc.perform(get("/favorites"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /favorites/{id} - Deve bloquear mutações anônimas")
    void shouldRejectAnonymousMutation() throws Exception {
        mockMvc.perform(post("/favorites/{trackId}", UUID.randomUUID()))
                .andExpect(status().is4xxClientError());
    }
}