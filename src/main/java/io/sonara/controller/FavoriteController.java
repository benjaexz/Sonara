package io.sonara.controller;

import io.sonara.dto.FavoriteResponseDTO;
import io.sonara.entity.User;
import io.sonara.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{trackId}")
    @ResponseStatus(HttpStatus.CREATED)
    public FavoriteResponseDTO addFavorite(
            @PathVariable UUID trackId,
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        return favoriteService.addFavorite(
                user.getEmail(),
                trackId
        );
    }

    @GetMapping
    public List<FavoriteResponseDTO> listFavorites(
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        return favoriteService.listFavorites(
                user.getEmail()
        );
    }

    @DeleteMapping("/{trackId}")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable UUID trackId,
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        favoriteService.removeFavorite(
                trackId,
                user.getEmail()
        );

        return ResponseEntity.noContent().build();
    }
}